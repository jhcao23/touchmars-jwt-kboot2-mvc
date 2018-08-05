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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
public class RestSignupController {
	
	private final Logger logger = LoggerFactory.getLogger(RestSignupController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private JwtTokenService jwtTokenService;

	public RestSignupController(){}

	@PostMapping(value="/rest/signup", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> signup(@Valid @RequestBody SignupForm form, BindingResult formBinding, WebRequest request, HttpServletResponse response) {
		if (formBinding.hasErrors() || form==null) {			
			for(ObjectError oe: formBinding.getAllErrors()) {
				return ResponseEntity.badRequest().body(String.format("invalid signup form: %s %s", oe.getObjectName(), oe.getDefaultMessage()));
			}
			return ResponseEntity.badRequest().body("invalid signup form!");
		}
		String error = null;
		HttpStatus httpStatus = HttpStatus.OK;
		String username = form.getUsername();
		String password = form.getPassword();
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			error = "username and password cannot be empty";
		}else {
			if(!isUsernameFormatOk(username)) {
				error = "username format is not okay";
			} else if(!isPasswordFormatOk(password)) {
				error = "password format is not okay";
			}
		}
		if(error==null) {
			Optional<TouchUser> accountO = userRepository.findByAccountUsername(username);
			if(accountO.isPresent()) {
				error = "username is already existing";
				httpStatus = HttpStatus.IM_USED;
			}
		}
		if(error!=null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			if(httpStatus!=null)
				return ResponseEntity.status(httpStatus).body(error);
			else
				return ResponseEntity.badRequest().body(error);
		}
		Authority authority = authorityRepository.findById(Authority.ID_ROLE_USER).orElse(null);
		try {			
			TouchUser user = createUser(form, formBinding, authority);
			//successfully created a User
			if (user != null) {
				SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(user.getHashId(), null, null)
				);
				String token = jwtTokenService.getToken4User(user);
				response.addHeader(AUTH_HEADER_NAME, token);
				return ResponseEntity.ok().build();//200
			}
		}catch(DataAccessException e) {
			logger.debug("DataAccessException: {}", e.getMessage());
		}catch(Exception e) {
			logger.error("Error: {}", e.getMessage());
		}
		return ResponseEntity.badRequest().body("internal error");//400
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
