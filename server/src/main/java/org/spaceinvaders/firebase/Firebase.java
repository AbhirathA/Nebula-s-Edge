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

package org.spaceinvaders.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.spaceinvaders.firebase.util.DatabaseAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class Firebase
{
    public static final String SERVICE_ACCOUNT_KEY = "serviceAccountKey.json";

    /**
     * @throws IOException                  If the firebase could not be initialized
     * @throws org.spaceinvaders.firebase.util.NetworkNotFoundException     If there is no network connection
     */
    private Firebase() throws IOException, org.spaceinvaders.firebase.util.NetworkNotFoundException
    {
        initializeFirebase();
    }

    //Holder class so that the Firebase is only initialized once
    private static class Holder
    {
        private static final Firebase INSTANCE;

        static
        {
            try
            {
                INSTANCE = new Firebase();
            }
            catch(IOException | org.spaceinvaders.firebase.util.NetworkNotFoundException e)
            {
                org.spaceinvaders.firebase.util.LoggerUtil.logError(e.getMessage());
                throw new org.spaceinvaders.firebase.util.NetworkNotFoundException("Cannot connect to the network.");
            }
        }
    }

    /**
     * Initializes the firebase database from the service account key provided in the "resources" folder.
     * @throws IOException if the service account key is missing
     * @throws org.spaceinvaders.firebase.util.NetworkNotFoundException if the program cannot connect to the network
     */
    private void initializeFirebase() throws IOException, org.spaceinvaders.firebase.util.NetworkNotFoundException
    {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(SERVICE_ACCOUNT_KEY))
        {
            if (serviceAccount == null)
                throw new IOException("Resource file 'serviceAccountKey.json' not found in resources.");

            try
            {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
            catch(IOException e)
            {
                throw new org.spaceinvaders.firebase.util.NetworkNotFoundException("Cannot connect to the network.");
            }
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
     * @throws DatabaseAccessException  if the user's  data could not be created
     */
    public void createUser(String email, String password) throws FirebaseAuthException, DatabaseAccessException
    {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        String userId = userRecord.getUid(); // Get the unique user ID

        addUserData(userId, email);
    }

    /**
     * When a new user is created, this function adds the default data for that user.
     * @param userId                        the unique user id of the user
     * @param email                         the email address of the user
     * @throws DatabaseAccessException      if any errors occurred in accessing the database
     */
    private void addUserData(String userId, String email) throws DatabaseAccessException
    {
        // Add additional user data to Firestore
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("level", 1);
        userData.put("points", 500);

        // Save the data in the Firestore users collection
        DocumentReference docRef = db.collection("users").document(userId);
        try
        {
            // Blocks until the write operation is done
            docRef.set(userData).get();
            org.spaceinvaders.firebase.util.LoggerUtil.logInfo("Created user: " + userId);
        }
        catch (Exception e)
        {
            org.spaceinvaders.firebase.util.LoggerUtil.logException("Error creating user: " + userId, e);
            throw new DatabaseAccessException("Error creating database for user: " + userId);
        }
    }
    /**
     * Method for checking if the email and password is correct
     * @param email                        the email id of the user
     * @param password                     the password of the user
     * @throws DatabaseAccessException     if any errors occurred in accessing the database
     */
    public String signinUser(String email, String password) throws DatabaseAccessException
    {
        return "";
    }
}
