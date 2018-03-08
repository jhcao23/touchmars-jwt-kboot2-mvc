package org.springframework.security.web.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import technology.touchmars.template.model.UserConnection;

import java.util.Collection;

public class UserConnectionAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private UserConnection userConnection;

    public UserConnectionAuthenticationToken(UserConnection userConnection, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.userConnection = userConnection;
    }

    public UserConnection getUserConnection() {
        return userConnection;
    }

}
