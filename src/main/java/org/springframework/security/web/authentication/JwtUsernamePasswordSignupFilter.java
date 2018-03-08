package org.springframework.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.Assert;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JwtTokenService;
import technology.touchmars.template.signup.SignupForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * Learn from UsernamePasswordAuthenticationFilter from Spring Security,
 * plus override AbstractAuthenticationProcessingFilter.successfulAuthentication.
 *  
 * @author jhcao
 *
 */
public class JwtUsernamePasswordSignupFilter extends AbstractAuthenticationProcessingFilter {

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	public static final String DEFAULT_REST_SIGNUP_URL = "/rest/signup";
	public static final String DEFAULT_WEB_SIGNUP_URL = "/signup/authenticate";

	//	private UserRepository userRepository;
	private boolean postOnly = true;
	private AuthorityRepository authorityRepository;
	private UserRepository userRepository;

	public JwtUsernamePasswordSignupFilter(
			UserRepository userRepository,
			AuthorityRepository authorityRepository,
			JwtTokenService jwtTokenService,
			JwtClaimService jwtClaimService) {
		super(
			new OrRequestMatcher(
				new AntPathRequestMatcher(DEFAULT_REST_SIGNUP_URL, HttpMethod.POST.name()),
				new AntPathRequestMatcher(DEFAULT_WEB_SIGNUP_URL, HttpMethod.POST.name())
			)
		);
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.setAuthenticationSuccessHandler(
				new JwtAuthenticationSuccessHandler(userRepository, jwtTokenService, jwtClaimService)
		);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (postOnly && !request.getMethod().trim().equalsIgnoreCase("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		String username = null, password = null;
		if(request.getMethod().trim().equalsIgnoreCase(HttpMethod.POST.name())) {
			String contentType = request.getContentType().toLowerCase();				
			if(contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
                SignupForm account = new ObjectMapper().readValue(request.getReader(), SignupForm.class);
                username = account.getUsername();
                password = account.getPassword();
                TouchUser touchUser = createUser(account);
                if(touchUser==null) {
                    throw new BadCredentialsException("failed to create a new account");
                }
			}
		}
		UsernamePasswordAuthenticationToken authRequest = 
			new UsernamePasswordAuthenticationToken(username, password);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

    private TouchUser createUser(SignupForm form) {
        try {
            Authority authority = this.authorityRepository.getOne(Authority.ID_ROLE_USER);
            TouchUser user = new TouchUser();
            user.assembleUser(form.getUsername(), form.getPassword(), form.getFirstName(), form.getLastName());
            user.addAuthority(authority);
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from the login
	 * request.
	 *
	 * @param usernameParameter the parameter name. Defaults to "username".
	 */
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	/**
	 * Sets the parameter name which will be used to obtain the password from the login
	 * request..
	 *
	 * @param passwordParameter the parameter name. Defaults to "password".
	 */
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
