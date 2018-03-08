package org.springframework.security.web.authentication;

import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.model.UserConnection;

public interface PostSignupService {

    public void onSignupSuccess(TouchUser touchUser, UserConnection userConnection);

}
