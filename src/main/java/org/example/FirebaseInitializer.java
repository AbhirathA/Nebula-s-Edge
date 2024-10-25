package org.example;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer
{
    private static void initializeFirebase()
    {
        try
        {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully!");
        }
        catch (IOException e)
        {
            System.out.println("Error initializing Firebase: " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        initializeFirebase();
    }
}
