package technology.touchmars.template.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper=false) 
@MappedSuperclass
public class ConnectionAttributes extends AuditableBaseEntity implements ConnectionProperty, EmailInterface {

	@Column(nullable = false, name="provider_id")
	private String providerId;
	
	@Column(nullable = false, name="provider_user_id")
	private String providerUserId;
	
	@Column(nullable = false, name="ranking")
	private Integer rank = 0;		
	//TODO: + appName
	
	@Column(nullable = true, name="display_name")
	private String displayName;	
	
	@Column(nullable = true, name="profile_url")
	private String profileUrl ;
	
	@Column(nullable = true, name="image_url")
	private String imageUrl ;
	
	@Column(nullable = true, name="access_token")
	private String accessToken ;
	
	@Column(nullable = true, name="secret")
	private String secret ;
	
	@Column(nullable = true, name="refresh_token")
	private String refreshToken ;
	
	@Column(nullable = true, name="expire_time")
	private Long expireTime ;
	
	@Column(name="token_type")
	private String tokenType;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="locale")
	private String locale;
	
	@Column(name="email")
	private String email;
	
	@Column(name="email_verified")
	private Boolean emailVerified;
	
	
	
}
