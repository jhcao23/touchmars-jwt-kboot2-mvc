package technology.touchmars.template.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Implementation of FirebaseDiscoveryService
 * sample:
 * firebase.apps = appId1, appId2, appId3
 *
 * firebase.appId1.app-id = APP-ID,
 * firebase.appId1.private-key = something.json,
 * firebase.appId1.database-url = URL
 *
 */
@RequiredArgsConstructor
//@AllArgsConstructor
public class FirebaseDiscoveryServiceImpl implements FirebaseDiscoveryService {
    public static final String DEFAULT_PREFIX = "firebase";
    public static final String DEFAULT_APP_NAME_SEPARATOR_REGEX = "\\s*,\\s*";
    public static final String DEFAULT_POSTFIX_APP_NAMES = "apps";
    public static final String DEFAULT_POSTFIX_APP_ID = "app-id";
    public static final String DEFAULT_POSTFIX_PRIVAEY_KEY_FILE_PATH = "private-key";
    public static final String DEFAULT_POSTFIX_DATABASE_URL = "database-url";

    @NonNull
    private Environment environment;

    private String prefix = DEFAULT_PREFIX;

    @Override
    public String[] getAppNames() {
        String appNames = environment.getProperty(prefix+"."+DEFAULT_POSTFIX_APP_NAMES);
        if(!StringUtils.hasText(appNames))
            return new String[0];
        return appNames.split(DEFAULT_APP_NAME_SEPARATOR_REGEX);
    }

    @Override
    public String getAppId(String appName) {
        return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_APP_ID);
    }

    @Override
    public String getPrivateKeyFilePath(String appName) {
        return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_PRIVAEY_KEY_FILE_PATH);
    }

    @Override
    public String getDatabaseUrl(String appName) {
        return environment.getProperty(prefix+"."+appName+"."+ DEFAULT_POSTFIX_DATABASE_URL);
    }
}
