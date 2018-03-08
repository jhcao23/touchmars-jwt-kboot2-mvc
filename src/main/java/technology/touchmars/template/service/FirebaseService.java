package technology.touchmars.template.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;

import java.util.Map;

public interface FirebaseService {

    public Map<String, FirebasePackage> getFirebaseMap();
    public String[] getAppNames();
    public FirebasePackage getFirebasePackage(String appName);
    public String getProjectId(String appName);
    public FirebaseApp getFirebaseApp(String appName);
    public FirebaseAuth getFirebaseAuth(String appName);
    public StorageClient getStorageClient(String appName);

}
