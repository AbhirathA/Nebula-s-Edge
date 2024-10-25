package org.example;

import com.google.auth.oauth2.GoogleCredentials;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public final class FirebaseInitializer
{
    public FirebaseInitializer() throws IOException
    {
        initializeFirebase();
    }

    private void initializeFirebase() throws IOException
    {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

    public void createUser(String email, String password) throws FirebaseAuthException
    {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        FirebaseAuth.getInstance().createUser(request);
    }

    public static void main(String [] args) throws IOException, FirebaseAuthException
    {
        FirebaseInitializer initializer = new FirebaseInitializer();
        initializer.createUser("hello@gamil.com", "hello12351!!!");
    }
}
