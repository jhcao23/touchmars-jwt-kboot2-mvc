package com.google.firebase;

public class FirebaseUtils {

    public static String getFirebaseAppProjectId(FirebaseApp app) {
        return app==null?null:app.getProjectId();
    }
}
