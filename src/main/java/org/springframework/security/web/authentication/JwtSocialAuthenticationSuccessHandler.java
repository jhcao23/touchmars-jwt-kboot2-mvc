package org.springframework.security.web.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.JwtRedirectStrategy;
import org.springframework.util.StringUtils;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JwtTokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static technology.touchmars.template.service.JwtTokenService.*;

/**
 * rather than extending the SavedRequestAwareAuthenticationSuccessHandler,
 * it should extend the simpler version - SimpleUrlAuthenticationSuccessHandler.
 * This is because we will assume angular/mobile will handle the process to redirect to the very initial request.
 * 
 * @author jhcao
 *
 */
public class JwtSocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(JwtSocialAuthenticationSuccessHandler.class);
	
	private UserRepository userRepository;
	private JwtTokenService jwtTokenService;
	
	public JwtSocialAuthenticationSuccessHandler(UserRepository userRepository, JwtTokenService jwtTokenService) {
		super();
		this.userRepository = userRepository;
		this.jwtTokenService = jwtTokenService;
		setRedirectStrategy(new JwtRedirectStrategy());
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
		throws IOException, ServletException {
		
		logger.debug("JwtSocialAuthenticationSuccessHandler.onAuthenticationSuccess");
		logger.debug("request servlet path is {}",request.getServletPath());
		
		if(authentication!=null){
			String hashId = authentication.getName();
			logger.debug("hashId={}", hashId);
			if(!StringUtils.isEmpty(hashId)){
				Optional<TouchUser> optional = userRepository.findByHashId(hashId);
				if(optional.isPresent()){
					logger.debug("user is present!");
					TouchUser user = optional.get();
					String token = jwtTokenService.getToken4User(user);
					logger.debug("token is {} to add to header {}", token, AUTH_HEADER_NAME);
					response.addHeader(AUTH_HEADER_NAME, token);
					String firebaseToken = jwtTokenService.getFirebaseToken4User(user);
					response.addHeader(AUTH_HEADER_NAME_FIREBASE, firebaseToken);


//					response.addCookie(createCookieForToken(token)); // we don't use cookie at all !
				}
			}
		}
		//finally call super
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
