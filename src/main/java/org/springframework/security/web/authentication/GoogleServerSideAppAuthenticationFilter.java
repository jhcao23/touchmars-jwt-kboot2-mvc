package org.springframework.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.model.UserConnection;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserConnectionRepository;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.GoogleAppDiscoveryService;
import technology.touchmars.template.service.GoogleSigninServiceUtil;
import technology.touchmars.template.service.JpaConnectionSignUp;
import technology.touchmars.template.service.JwtTokenService;
import technology.touchmars.template.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 	This is the API|URL for login requested from Google Signin.
 * 
 * 	<a href="https://developers.google.com/identity/sign-in/web/server-side-flow">Google Signin Server Side</a>
 * 
 * 	Learn from UsernamePasswordAuthenticationFilter from Spring Security,
 * 	plus override AbstractAuthenticationProcessingFilter.successfulAuthentication.
 *  
 * @author jhcao
 *
 */
public class GoogleServerSideAppAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String DEFAULT_REST_LOGIN_URL = "/rest/signin/google";
	public static final int DEFAULT_JWT_EXPIRY = 1000*3600*24*10;
	
	private GoogleAppDiscoveryService googleAppDiscouveryService;
	private RequestAuthorityService requestAuthorityService; // nullable
	private PostSignupService postSignupService; // nullable
	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	private UserConnectionRepository userConnectionRepository;
	private boolean postOnly = true;
	
	public GoogleServerSideAppAuthenticationFilter(
		GoogleAppDiscoveryService googleAppDiscouveryService,
		RequestAuthorityService requestAuthorityService,
		PostSignupService postSignupService,
		UserRepository userRepository,
		UserConnectionRepository userConnectionRepository, 
		AuthorityRepository authorityRepository,
        JwtTokenService jwtTokenService,
        JwtClaimService jwtClaimService,
        String url) {
		super(new AntPathRequestMatcher(StringUtils.hasText(url)?url:DEFAULT_REST_LOGIN_URL.toLowerCase(), "POST"));
		this.googleAppDiscouveryService = googleAppDiscouveryService;
		this.requestAuthorityService = requestAuthorityService;
		this.postSignupService = postSignupService;
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.userConnectionRepository = userConnectionRepository;
        this.setAuthenticationFailureHandler(new SimpleRestFailureHandler());
		this.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(userRepository, jwtTokenService, jwtClaimService));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		if (postOnly && !request.getMethod().trim().equalsIgnoreCase(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String username = null;
		if(request.getMethod().trim().equalsIgnoreCase(HttpMethod.POST.name())) {
			String contentType = request.getContentType().toLowerCase();
			logger.debug("contentType is {}", contentType);
			
			if(contentType.contains("json")) {
				String origin = request.getHeader("origin");
//				String referer = request.getHeader("referer");

				GoogleCode gCode = new ObjectMapper().readValue(request.getReader(), GoogleCode.class);
				String clientId = this.googleAppDiscouveryService.getClientId(gCode.getAppName());
				String secret = this.googleAppDiscouveryService.getSecret(gCode.getAppName());
				
				String redirectUri = ObjectUtils.firstNonNull(origin, gCode.redirectUri, this.googleAppDiscouveryService.getDefaultRedirectUri(gCode.getAppName()));
				long timestamp = System.currentTimeMillis();	
				timestamp += DEFAULT_JWT_EXPIRY;
				try {
					UserConnection uc = GoogleSigninServiceUtil.loadCode(gCode.getCode(), clientId, secret, redirectUri);	
					
					Optional<UserConnection> ucO = userConnectionRepository.findByProviderIdAndProviderUserId(GoogleSigninServiceUtil.DEFAULT_PROVIDER_ID, uc.getProviderUserId());
					TouchUser user = null;
					if(!ucO.isPresent()) {//create a User with new UserConnectionGoogle
						Integer[] roles = requestAuthorityService==null?null:requestAuthorityService.getAuthoritiesFromRequestAndUserConnection(request, uc);
						TouchUser user0 = JpaConnectionSignUp.createUser4Connection(authorityRepository, roles);					
						user0.addConnection(uc);
						user = userRepository.save(user0);
						// after sign up success if not require email validation... well yes, since this is gmail!
						if(this.postSignupService!=null){
							this.postSignupService.onSignupSuccess(user, uc);
						}
						uc = userConnectionRepository
								.findByProviderIdAndProviderUserId(
										GoogleSigninServiceUtil.DEFAULT_PROVIDER_ID,
										uc.getProviderUserId()
								)
								.orElse(null);
					}else {//existing User simply login
						UserConnection userConnection = ucO.get();
						//update sessionKey etc.
						GoogleSigninServiceUtil.copyUserConnection(uc, userConnection);
						userConnection = userConnectionRepository.save(userConnection);
						// update TouchUser and UserConnection
						user = userConnection.getTouchUser();
						uc = userConnection;
					}	
					if(user!=null) {
						username = user.getHashId();
						username = username.trim();
						if (!StringUtils.hasText(username)) {
							username = "";
						}
						//add Authority
						List<GrantedAuthority> grantedAuthorities = 
							user.getAuthorityList()
							.stream()
							.map( a->new SimpleGrantedAuthority(a.getName())).collect(Collectors.toList() );
						response.setHeader(JwtAuthenticationSuccessHandler.EXPIRE, ""+timestamp);
						UserConnectionAuthenticationToken authToken =
								new UserConnectionAuthenticationToken(uc, username, "", grantedAuthorities);
						//TODO: let's make it simple, simply return an authenticated one so bypass getAuthenticationManager().authenticate!
	//					return this.getAuthenticationManager().authenticate(authToken);
						return authToken;
					}

				}catch(IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}			
			
		}
		throw new AuthenticationCredentialsNotFoundException("code not valid");
		
	}
	
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

}

@Data
class GoogleCode {
	String appName;
	String redirectUri;
	String code;	
}

