package technology.touchmars.template.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static technology.touchmars.template.model.Authority.DEFAULT_ROLE_USER;
import static technology.touchmars.template.util.MapUtils.makeMap;

public class JwtTokenServiceImpl implements JwtTokenService {

	private final static Logger logger = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

	public static final String DEFAULT_SECRET_KEY = "secretKey";
	private final String SECRET_KEY;

	private static final String AUTHORITIES_KEY = "auth";

	public static final String PROJECT_ID = "pid";

	private FirebaseService firebaseService;

	public JwtTokenServiceImpl(
	        FirebaseService firebaseService,
            ApplicationPropertiesService applicationPropertiesService) {
		this.firebaseService = firebaseService;

        if(applicationPropertiesService!=null && StringUtils.hasText(applicationPropertiesService.getSecretKey())) {
            this.SECRET_KEY = applicationPropertiesService.getSecretKey();
        } else {
            this.SECRET_KEY = DEFAULT_SECRET_KEY;
        }
	}

	public static Long defaultExpiry() {
		return Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()).getTime();
	}

	public String getFirebaseToken4User(String userHashId, Collection<Authority> authorityList, Long expires, Map<String, Object> extraData){
		try {
		    Map<String, String> tokenMap = new HashMap<String, String>();
		    for(String appName: this.firebaseService.getAppNames()) {
		        String projectId = this.firebaseService.getProjectId(appName);
                Map<String, Object> additionalClaims = makeMap(extraData);
                additionalClaims.put(AUTHORITIES_KEY, getRoles4Frontend(authorityList));
                additionalClaims.put(PROJECT_ID, projectId);
                FirebaseAuth firebaseAuth = this.firebaseService.getFirebaseAuth(appName);
                String customToken = firebaseAuth.createCustomTokenAsync(userHashId, additionalClaims).get();
                tokenMap.put(projectId, customToken);
                logger.debug("firebase customToken for app {} is {}", appName, customToken);
            }
            ObjectMapper om = new ObjectMapper();
		    String fwt = om.writeValueAsString(tokenMap);
		    logger.debug("final fwt {}", fwt);
		    return fwt;
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getToken4User(String userHashId, Collection<Authority> authorityList, Long expires, Map<String, Object> extraData) {
		String authorities = getRoles4Frontend(authorityList);
		if(!StringUtils.hasText(authorities))
			authorities = DEFAULT_ROLE_USER;
		if(expires==null) {
			expires = defaultExpiry();
		}
		Map<String, Object> claims = makeMap(extraData);
		String result = 
			Jwts.builder()
					.setClaims(claims)
					.setSubject(userHashId)
					.claim(AUTHORITIES_KEY, authorities)
					.setIssuedAt(Calendar.getInstance().getTime())
					.setExpiration(new Date(expires))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
					.compact();
		return result;
	}
	
	public String getToken4User(TouchUser user){
		long expiry = defaultExpiry();
		return getToken4User(user.getHashId(), user.getAuthorityList(), expiry, null);
	}

	public String getFirebaseToken4User(TouchUser user){
		long expiry = defaultExpiry();
		return getFirebaseToken4User(user.getHashId(), user.getAuthorityList(), expiry, null);
	}

	public static Collection<GrantedAuthority> getGrantedAuthorityList(Claims claim){
		String authorities = claim.get(AUTHORITIES_KEY, String.class);
		return Stream.of(authorities.split("\\s*,\\s*"))
					.map(a->new SimpleGrantedAuthority(a))
					.collect(Collectors.toList());
	}

	public JwtFullInfo getFullInfo(String token){
		if(token!=null){
			try{
				Claims claim = getClaims(token);
				Date expiry = claim.getExpiration();
				String hashId = claim.getSubject();
				Collection<GrantedAuthority> grantedAuthorityList = getGrantedAuthorityList(claim);
				return new JwtFullInfo(hashId, expiry, grantedAuthorityList);
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
		
	}

	public Claims getClaims(String token){
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

	public static String getRoles4Frontend(Collection<Authority> authorities) {
		return authorities.stream()
				.map(a -> a.getName().toLowerCase())
				.map(a -> a.replace("role_", ""))
				.collect(Collectors.joining(","));
	}

	
}

//		try {
//			String subject =
//				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(result).getBody().getSubject();
//			if(!subject.equals(userHashId)) {
//				System.err.println(String.format("SHIT! %s != %s", userHashId, subject));
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}

//    public Date getExpiration(String token){
//        if(token!=null){
//            try{
//                Claims claim = getClaims(token);
//                return claim.getExpiration();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public String getAuthorities(String token){
//        if(token!=null){
//            try{
//                Claims claim = getClaims(token);
//                String authorities = claim.get(AUTHORITIES_KEY, String.class);
//                return authorities;
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public Collection<GrantedAuthority> getGrantedAuthorityList(String token){
//        if(token!=null){
//            try{
//                Claims claim = getClaims(token);
//                return getGrantedAuthorityList(claim);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public String getHashId(String token){
//        if(token!=null){
//            try{
//                Claims claim = getClaims(token);
//                return claim.getSubject();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public FirebaseApp buildFirebaseApp(String filename, String databaseUrl) {
//        try {
//            InputStream serviceAccount =
//                    getClass().getResourceAsStream("/firebase/"+filename);
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl(databaseUrl)
//                    .build();
//            return FirebaseApp.initializeApp(options, "app");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

