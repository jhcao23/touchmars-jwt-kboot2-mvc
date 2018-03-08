package technology.touchmars.template.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JwtTokenService {

//	private final static Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

	public static final String ROLES_HEADER_NAME = "roles";
	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	public static final String AUTH_HEADER_NAME_FIREBASE = "F-AUTH-TOKEN";
	public static final String AUTH_HEADER_REFRESH = "refresh";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";

	public static final String AUTHORITIES_KEY = "auth";

	public String getFirebaseToken4User(String userHashId, Collection<Authority> authorityList, Long expires, Map<String, Object> extraData);

    public String getFirebaseToken4User(TouchUser user);

    public String getToken4User(String userHashId, Collection<Authority> authorityList, Long expires, Map<String, Object> extraData);
	
	public String getToken4User(TouchUser user);

	public JwtFullInfo getFullInfo(String token);

	public Claims getClaims(String token);

    public static Long defaultExpiry() {
        return Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    public static Collection<GrantedAuthority> getGrantedAuthorityList(Claims claim){
        String authorities = claim.get(AUTHORITIES_KEY, String.class);
        return Stream.of(authorities.split("\\s*,\\s*"))
                .map(a->new SimpleGrantedAuthority(a))
                .collect(Collectors.toList());
    }

	public static String getRoles4Frontend(Collection<Authority> authorities) {
		return authorities.stream()
				.map(a -> a.getName().toLowerCase())
				.map(a -> a.replace("role_", ""))
				.collect(Collectors.joining(","));
	}

}