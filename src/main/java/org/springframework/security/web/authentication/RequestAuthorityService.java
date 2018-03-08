package org.springframework.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

import technology.touchmars.template.model.UserConnection;

public interface RequestAuthorityService {
	
	public Integer[] getAuthoritiesFromRequest(HttpServletRequest request);
	
	public Integer[] getAuthoritiesFromRequestAndUserConnection(HttpServletRequest request, UserConnection userConnection);

}
