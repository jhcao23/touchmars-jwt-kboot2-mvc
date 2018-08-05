package technology.touchmars.template.service;

import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserRepository;

public class JpaConnectionSignUp  {

	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	
	public JpaConnectionSignUp(UserRepository userRepository, AuthorityRepository authorityRepository){
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}

	public static TouchUser createUser4Connection(AuthorityRepository authorityRepository, Integer...roles) {
		TouchUser user = new TouchUser();
		String hashId = GenerateUniqueKey.getInstance().generateUniqueHashId();
		user.setHashId(hashId);
		//add ROLE_USER
		if(roles==null) {
			Authority authority = authorityRepository.findById(Authority.ID_ROLE_USER).orElse(null);
			user.addAuthority(authority);
		}else {
			for(Integer role: roles) {
				Authority authority = authorityRepository.findById(role).orElse(null);
				user.addAuthority(authority);
			}
		}
		return user;
	}

}
