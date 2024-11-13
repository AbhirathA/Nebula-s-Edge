package org.spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.io.InputStream;

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
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json"))
        {
            if (serviceAccount == null) {
                throw new IOException("Resource file 'serviceAccountKey.json' not found in resources.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
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

        try
        {
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
        }
        catch(Exception e)
        {
            System.out.println("Failed to create new user: " + e.getMessage());
        }
    }
}
