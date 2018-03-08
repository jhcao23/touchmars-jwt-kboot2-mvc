package technology.touchmars.template.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FirebaseServiceImpl implements FirebaseService {
    public static final String FIREBASE_FOLDER_NAME_INSIDE_RESOURCE = "firebase";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private final static Logger logger = LoggerFactory.getLogger(FirebaseServiceImpl.class);

    private static Map<String, FirebasePackage> firebaseMap = new HashMap<String, FirebasePackage>();

    private FirebaseDiscoveryService firebaseDiscoveryService;

    public FirebaseServiceImpl(FirebaseDiscoveryService firebaseDiscoveryService) {
        this.firebaseDiscoveryService = firebaseDiscoveryService;
        String[] appNames = this.firebaseDiscoveryService.getAppNames();
        logger.debug("appNames {}", appNames);
        for(String appName: appNames) {
            String appId = this.firebaseDiscoveryService.getAppId(appName);
            String databaseUrl = this.firebaseDiscoveryService.getDatabaseUrl(appName);
            String fileName = this.firebaseDiscoveryService.getPrivateKeyFilePath(appName);
            logger.debug("APP {} has appId {}, database-url {}, fileName {}", appName, appId, databaseUrl, fileName);
            FirebaseApp app = this.buildFirebaseApp(appName, fileName, databaseUrl);
            FirebasePackage firebasePackage = new FirebasePackage(app);
            this.firebaseMap.put(appName, firebasePackage);
        }
    }

    public FirebaseApp buildFirebaseApp(String appName, String fileName, String databaseUrl) {
        try {
            String filePath = FILE_SEPARATOR + FIREBASE_FOLDER_NAME_INSIDE_RESOURCE + FILE_SEPARATOR + fileName;
            logger.debug("filePath {}", filePath);
            InputStream credentialsStream = getClass().getResourceAsStream(filePath);
            logger.debug("credentialsStream", credentialsStream);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setDatabaseUrl(databaseUrl)
                    .build();
            return FirebaseApp.initializeApp(options, appName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, FirebasePackage> getFirebaseMap() {
        return firebaseMap;
    }

    @Override
    public String[] getAppNames() {
        return firebaseMap.keySet().stream().toArray(size -> new String[size]);
    }

    @Override
    public FirebasePackage getFirebasePackage(String appName) {
        return firebaseMap.get(appName);
    }

    @Override
    public String getProjectId(String appName) {
        return getFirebasePackage(appName).getProjectId();
    }

    @Override
    public FirebaseApp getFirebaseApp(String appName) {
        return firebaseMap.get(appName).getFirebaseApp();
    }

    @Override
    public FirebaseAuth getFirebaseAuth(String appName) {
        return firebaseMap.get(appName).getFirebaseAuth();
    }

    @Override
    public StorageClient getStorageClient(String appName) {
        return firebaseMap.get(appName).getStorageClient();
    }
}
