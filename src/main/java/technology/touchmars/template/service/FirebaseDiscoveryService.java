package technology.touchmars.template.service;

public interface FirebaseDiscoveryService {

    public String[] getAppNames();
    public String getAppId(String appName);
    public String getPrivateKeyFilePath(String appName);
    public String getDatabaseUrl(String appName);

}
