/**
 * 
 */
package org.springframework.security.web.authentication;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JwtTokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Serve for JwtUsernamePasswordAuthenticationFilter.
 * At the end of successfulAuthentication, call this handler's default method so that JWTs is set in header.
 * @author jhcao
 *
 */
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	public static final String EXPIRE = "expire";
	
	private UserRepository userRepository;
	private JwtTokenService jwtTokenService;
	private JwtClaimService jwtClaimService;

	private JwtAuthenticationSuccessHandler(UserRepository userRepository, JwtTokenService jwtTokenService) {
		super();
		this.userRepository = userRepository;
		this.jwtTokenService = jwtTokenService;
	}

	public JwtAuthenticationSuccessHandler(UserRepository userRepository, JwtTokenService jwtTokenService, JwtClaimService jwtClaimService) {
		this(userRepository, jwtTokenService);
		this.jwtClaimService = jwtClaimService;
	}
	
	//we don't need redirect so no need for defaultTargetUrl
//	public JwtAuthenticationSuccessHandler(UserRepository userRepository, String defaultTargetUrl) {
//		super(defaultTargetUrl);
//		this.userRepository = userRepository;
//	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
//		Object principal = authentication.getPrincipal();
		String username = authentication.getName();
		if(!authentication.isAuthenticated() || StringUtils.isEmpty(username))
			return ;
		String expire = response.getHeader(EXPIRE);
		Long timestamp = null;
		try {
			timestamp = Long.parseLong(expire);
		}catch (Exception e) {
			e.printStackTrace();
		}
		Optional<TouchUser> optional = userRepository.findByHashId(username); 
		if(optional.isPresent()){
			TouchUser user = optional.get();
			List<Authority> authorities = user.getAuthorityList();
			String token =
					this.jwtTokenService.getToken4User(
							user.getHashId(), authorities, timestamp,
							jwtClaimService==null?null:jwtClaimService.claims4UserConnection(authentication)
					);
			response.addHeader(JwtTokenService.AUTH_HEADER_NAME, token);
			String firebaseToken =
					this.jwtTokenService.getFirebaseToken4User(
							user.getHashId(), authorities, timestamp,
							jwtClaimService==null?null:jwtClaimService.claims4UserConnection(authentication)
					);
			response.addHeader(JwtTokenService.AUTH_HEADER_NAME_FIREBASE, firebaseToken);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			// authorities is in JWT so no need to add ROLES_HEADER_NAME
//			response.addHeader(JwtTokenService.ROLES_HEADER_NAME, getRoles4Frontend(authorities));
			
		}

	}

}
