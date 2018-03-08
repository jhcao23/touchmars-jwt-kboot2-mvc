package org.springframework.security.web.authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JwtFullInfo;
import technology.touchmars.template.service.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	public static final String COLON = ":";
	
	private UserRepository userRepository;
	private JwtTokenService jwtTokenService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		Assert.isInstanceOf(HttpServletRequest.class, request, "request must be HttpServletRequest");
		Assert.isInstanceOf(HttpServletResponse.class, response, "response must be HttpServletResponse");
		
		if( setAuthenticationFromHeader((HttpServletRequest) request, (HttpServletResponse)response) ) {			
			chain.doFilter(request, response);
		}else {
			//JWT authentication failed then return! 
		}

	}

	private boolean setAuthenticationFromHeader(HttpServletRequest request, HttpServletResponse response) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null){
	        final String authHeader = request.getHeader(JwtTokenService.HEADER_AUTHORIZATION);
	        if(authHeader!=null) {//from now on, confirm header has Authorization, then JWT authentication starts!
		        final String token = getTokenFromHeader(authHeader);
		        if(token!=null){	        	
		        		JwtFullInfo info = this.jwtTokenService.getFullInfo(token);
			        	String hashId = info.getHashId();
			        	Date expiry = info.getExpiry();
			        	Collection<GrantedAuthority> grantedAuthorityList = info.getGrantedAuthorityList();
			        	if(expiry!=null && expiry.getTime()>System.currentTimeMillis() && hashId!=null){
			        		Optional<TouchUser> touchUser0 = userRepository.findByHashId(hashId);
			        		if(touchUser0.isPresent()) {
				        		SecurityContextHolder.getContext().setAuthentication(
				        			new UsernamePasswordAuthenticationToken(touchUser0.get(), null, grantedAuthorityList)
				        		);
			        		} else {
			        			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			        			return false;
			        		}
			        	}else{
			        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    			response.addHeader(JwtTokenService.HEADER_AUTHORIZATION, JwtTokenService.AUTH_HEADER_REFRESH);
			    			return false; //only fail when [Authorization: Bearer $JWT] has token and fails!
			        	}
		        }
	        }
		}
		return true;
		
	}
	
	private String getTokenFromHeader(String authHeader){
		if(authHeader!=null && authHeader.trim().startsWith(JwtTokenService.BEARER)){
	        	String token = StringUtils.trimLeadingWhitespace(authHeader.substring(JwtTokenService.BEARER.length()));	        	
	        	return token;
		}
		return null;
		
	}

}
