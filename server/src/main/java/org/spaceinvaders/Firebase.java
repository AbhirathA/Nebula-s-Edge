package org.spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.io.FileInputStream;

public final class Firebase
{
    private Firebase() throws IOException
    {
        initializeFirebase();
    }

    private static class Holder
    {
        private static final Firebase INSTANCE;

        static
        {
            try
            {
                INSTANCE = new Firebase();
            }
            catch(IOException e)
            {
                throw new ExceptionInInitializerError("Failed to set up the server: " + e.getMessage());
            }
        }
    }

    private void initializeFirebase() throws IOException
    {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

    public static Firebase getInstance()
    {
        return Holder.INSTANCE;
    }

    public void createUser(String email, String password) throws FirebaseAuthException
    {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        FirebaseAuth.getInstance().createUser(request);
    }
}
