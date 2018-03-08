package technology.touchmars.template.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirebasePackage {
    private final static Logger logger = LoggerFactory.getLogger(FirebasePackage.class);

    private String projectId;
    private FirebaseApp firebaseApp;

    private FirebaseAuth firebaseAuth;
    private StorageClient storageClient;

    public FirebasePackage(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
        this.projectId = FirebaseUtils.getFirebaseAppProjectId(firebaseApp);
        this.firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        this.storageClient = StorageClient.getInstance(firebaseApp);
    }

    public String getProjectId() {
        return projectId;
    }
    public FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
    public StorageClient getStorageClient() {
        return storageClient;
    }

}
