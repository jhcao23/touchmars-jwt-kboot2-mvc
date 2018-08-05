/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package technology.touchmars.template.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JwtTokenService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

import static technology.touchmars.template.service.JwtTokenService.AUTH_HEADER_NAME;

@Controller
public class SignupController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
    @Autowired
	private JwtTokenService jwtTokenService;

	public SignupController() { }

	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request, HttpServletResponse response) {
		String error = null;
		if (formBinding.hasErrors() || form==null) {
			error = "signup form has error";			
		}
		String username = form.getUsername();
		String password = form.getPassword();
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			error = "username and password cannot be empty";
		}else {
			if(!isUsernameFormatOk(username)) {
				error = "username format is not okay";
			}
			if(!isPasswordFormatOk(password)) {
				error = "password format is not okay";
			}
		}
		Optional<TouchUser> accountO = userRepository.findByAccountUsername(username);
		if(accountO.isPresent()) {
			error = "username is already existing";
		}		
		if(error!=null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return error;
		}
		Authority authority = authorityRepository.findById(Authority.ID_ROLE_USER).orElse(null);
		TouchUser user = createUser(form, formBinding, authority);
		if (user != null) {
			SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user.getHashId(), null, null)
			);
			String token = jwtTokenService.getToken4User(user);
			response.addHeader(AUTH_HEADER_NAME, token);
			return null;
		}
		error = "failed to create user due to server internal error";
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);		
		return error;
	}

	// internal helpers
	
	private TouchUser createUser(SignupForm form, BindingResult formBinding, Authority authority) {
		try {
			TouchUser user = new TouchUser();
			user.assembleUser(form.getUsername(), form.getPassword(), form.getFirstName(), form.getLastName());
			user.addAuthority(authority);
			return userRepository.save(user);			
		} catch (Exception e) {
			e.printStackTrace();
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
			return null;
		}
	}
	
	public boolean isUsernameFormatOk(String username) {
		return true;
	}
	public boolean isPasswordFormatOk(String password) {
		return true;
	}

}
