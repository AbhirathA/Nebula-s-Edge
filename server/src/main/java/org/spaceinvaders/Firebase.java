/**
 * Firebase.java
 * This class facilitates communication between the server and a Firebase
 * database. It provides methods to manage user state, retrieve and update
 * stored data, and handle user authentication. The class supports CRUD
 * operations and handles common exceptions related to database access.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/13/2024
 */

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
    public static final String SERVICE_ACCOUNT_KEY = "serviceAccountKey.json";

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

    /**
     * Initializes the firebase database from the service account key provided in the "resources" folder.
     * @throws IOException if the service account key is missing
     */
    private void initializeFirebase() throws IOException
    {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(SERVICE_ACCOUNT_KEY))
        {
            if (serviceAccount == null)
                throw new IOException("Resource file 'serviceAccountKey.json' not found in resources.");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    /**
     * Returns the firebase instance
     * @return the firebase instance
     */
    public static Firebase getInstance()
    {
        return Holder.INSTANCE;
    }

    /**
     * Creates a user from the given email address and password
     * @param email                     the email address of the user
     * @param password                  the password of the user
     * @throws FirebaseAuthException    if the user with the given username already exists (or password is too weak)
     */
    public void createUser(String email, String password) throws FirebaseAuthException
    {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        FirebaseAuth.getInstance().createUser(request);
    }
}
