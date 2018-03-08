package technology.touchmars.template.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import technology.touchmars.template.model.UserConnection;

import java.io.IOException;

public class GoogleSigninServiceUtil {

	public static final String DEFAULT_PROVIDER_ID = "google";
	public static final String TOKEN_SERVER_ENCODED_URL = "https://www.googleapis.com/oauth2/v4/token";
	public static final String LOCALE = "locale";
	public static final String PICTURE = "picture";
	public static final String NAME = "name";
	public static final String LAST_NAME = "family_name";
	public static final String FIRST_NAME = "given_name";

	public static UserConnection loadCode(String authCode, String clientId, String secret, String redirectUri) throws IOException {
		
		GoogleTokenResponse tokenResponse =
		          new GoogleAuthorizationCodeTokenRequest(
		              new NetHttpTransport(),
		              JacksonFactory.getDefaultInstance(),
		              TOKEN_SERVER_ENCODED_URL,
		              clientId,
		              secret,
		              authCode,
		              redirectUri)	// Specify the same URI of your website from which you login. 
		              .execute();

		String accessToken = tokenResponse.getAccessToken();
		Long expiry = tokenResponse.getExpiresInSeconds();
		String refreshToken = tokenResponse.getRefreshToken();
		String tokenType = tokenResponse.getTokenType();
		
		// Use access token to call API
//		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		
		// Get profile info from ID token
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject();  // Use this value as a key to identify a user.
		String email = payload.getEmail();
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get(NAME);
		String pictureUrl = (String) payload.get(PICTURE);
		String locale = (String) payload.get(LOCALE);
		String lastName = (String) payload.get(LAST_NAME);
		String firstName = (String) payload.get(FIRST_NAME);
		UserConnection uc = new UserConnection();
		uc.setAccessToken(accessToken);
		uc.setRefreshToken(refreshToken);
		uc.setExpireTime(expiry);
		uc.setTokenType(tokenType);
		uc.setProviderId(DEFAULT_PROVIDER_ID);
		uc.setProviderUserId(userId);
		uc.setDisplayName(name);
		uc.setImageUrl(pictureUrl);
		uc.setFirstName(firstName);
		uc.setLastName(lastName);
		uc.setLocale(locale);
		uc.setEmail(email);
		uc.setEmailVerified(emailVerified);		
		return uc;		
		
	}

	public static void copyUserConnection(UserConnection from, UserConnection to) {
		if(from!=null && to!=null) {
			to.setAccessToken(from.getAccessToken());
			to.setRefreshToken(from.getRefreshToken());
			to.setExpireTime(from.getExpireTime());
			to.setTokenType(from.getTokenType());
			to.setProviderId(DEFAULT_PROVIDER_ID);
			to.setProviderUserId(from.getProviderUserId());
			to.setDisplayName(from.getDisplayName());
			to.setImageUrl(from.getImageUrl());
			to.setFirstName(from.getFirstName());
			to.setLastName(from.getLastName());
			to.setLocale(from.getLocale());
			to.setEmail(from.getEmail());
			to.setEmailVerified(from.getEmailVerified());
		}

	}

}
