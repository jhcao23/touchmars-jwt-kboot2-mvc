package technology.touchmars.template.service;

import org.springframework.core.env.Environment;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class GoogleAppDiscoveryServiceImpl implements GoogleAppDiscoveryService {

	public static final String DEFAULT_PREFIX = "google";
	public static final String DEFAULT_POSTFIX_APP_ID = "client-id";
	public static final String DEFAULT_POSTFIX_SECRET = "secret";
	public static final String DEFAULT_POSTFIX_REDIRECT_URI = "redirect-uri";
	
	private String prefix = DEFAULT_PREFIX;
	@NonNull private Environment environment;
	
	@Override
	public String getClientId(String appName) {
		return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_APP_ID);
	}

	@Override
	public String getSecret(String appName) {
		return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_SECRET);
	}

	@Override
	public String getDefaultRedirectUri(String appName) {
		return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_REDIRECT_URI);
	}

}
