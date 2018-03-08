package org.springframework.security.web.authentication;

import org.springframework.security.core.Authentication;

import java.util.Map;

public interface JwtClaimService {

    public Map<String, Object> claims4UserConnection(Authentication authentication);

}
