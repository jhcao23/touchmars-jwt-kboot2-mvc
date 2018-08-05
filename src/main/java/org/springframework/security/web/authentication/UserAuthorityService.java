package org.springframework.security.web.authentication;

import technology.touchmars.template.model.UserConnection;

import java.util.Optional;

public interface UserAuthorityService {

    public UserConnection addAuthority2TouchUser(Optional<UserConnection> ucO, Integer[] roles);
}
