package technology.touchmars.template.service;

public interface GoogleAppDiscoveryService {
	
	public String getClientId(String appName);
	public String getSecret(String appName);
	public String getDefaultRedirectUri(String appName);

}
