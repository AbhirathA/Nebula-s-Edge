package org.spaceinvaders.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.spaceinvaders.firebase.util.DatabaseAccessException;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.NetworkNotFoundException;
import org.spaceinvaders.util.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Firebase.java
 * <br>
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
 * @version 1.2
 * @since 11/13/2024
 */
public final class Firebase {
    /**
     * The filename of the service account key JSON file used for authentication
     * with Firebase services.
     */
    public static final String SERVICE_ACCOUNT_KEY = "serviceAccountKey.json";

    /**
     * The URL of the Firebase Realtime Database associated with the application.
     */
    public static final String FIREBASE_DATABASE = "https://nebula-s-edge-6e33d-default-rtdb.firebaseio.com";

    /**
     * The default Firebase Realtime Database URL.
     * Update this URL to point to the correct database location.
     */
    private static final String DATABASE_URL = "https://nebula-s-edge-6e33d-default-rtdb.firebaseio.com/";

    /**
     * Json containing all the server constants required by the server.
     */
    public static final JsonObject serverConstants = fetchServerConstants();

    /**
     * Initializes the Firebase Instance and updates the Firebase Realtime Database with the
     * Server IP Address and related info.
     *
     * @throws IOException              If the firebase could not be initialized
     * @throws NetworkNotFoundException If there is no network connection
     */
    private Firebase() throws IOException, NetworkNotFoundException {
        initializeFirebase();
    }

    // Holder class so that the Firebase is only initialized once
    private static class Holder {
        private static final Firebase INSTANCE;

        static {
            try {
                INSTANCE = new Firebase();
            } catch (IOException | NetworkNotFoundException e) {
                LoggerUtil.logError(e.getMessage());
                throw new NetworkNotFoundException("Cannot connect to the network.");
            }
        }
    }

    /**
     * Initializes the firebase database from the service account key provided in
     * the "resources" folder.
     * 
     * @throws IOException              if the service account key is missing
     * @throws NetworkNotFoundException if the program cannot connect to the network
     */
    private void initializeFirebase() throws IOException, NetworkNotFoundException {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(SERVICE_ACCOUNT_KEY)) {
            if (serviceAccount == null)
                throw new IOException("Resource file 'serviceAccountKey.json' not found in resources.");

            try {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(FIREBASE_DATABASE)
                        .build();

                FirebaseApp.initializeApp(options);

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("serverInfo");

                Map<String, Object> data = new HashMap<>();
                data.put("ip", ServerInfo.IP);
                data.put("http-port", ServerInfo.HTTP_PORT);
                data.put("udp-port", ServerInfo.UDP_PORT);

                databaseRef.setValueAsync(data);

                LoggerUtil.logInfo("Server Info Updated Successfully");
            } catch (IOException e) {
                throw new NetworkNotFoundException("Cannot connect to the network.");
            }
        }
    }

    /**
     * Returns the firebase instance
     * 
     * @return the firebase instance
     */
    public static Firebase getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Creates a user from the given email address and password
     * 
     * @param email    the email address of the user
     * @param password the password of the user
     * @throws FirebaseAuthException   if the user with the given username already
     *                                 exists (or password is too weak)
     * @throws DatabaseAccessException if the user's data could not be created
     */
    public void createUser(String email, String password) throws FirebaseAuthException, DatabaseAccessException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        String userId = userRecord.getUid(); // Get the unique user ID

        addUserData(userId, email);
    }

    /**
     * When a new user is created, this function adds the default data for that
     * user.
     * 
     * @param userId the unique user id of the user
     * @param email  the email address of the user
     * @throws DatabaseAccessException if any errors occurred in accessing the
     *                                 database
     */
    private void addUserData(String userId, String email) throws DatabaseAccessException {
        // Add additional user data to Firestore
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("level", 1);
        userData.put("killCount", 0);

        // Save the data in the Firestore users collection
        DocumentReference docRef = db.collection("users").document(userId);
        try {
            // Blocks until the write operation is done
            docRef.set(userData).get();
            LoggerUtil.logInfo("Created user: " + userId);
        } catch (Exception e) {
            LoggerUtil.logException("Error creating user: " + userId, e);
            throw new DatabaseAccessException("Error creating database for user: " + userId);
        }
    }

    /**
     * Retrieves user data from the database by the JWT id token of the user
     * @param idToken the JWT id token
     * @return A map representation of all the data belonging to this user
     * @throws DatabaseAccessException  If the data could not be accessed for this user
     * @throws FirebaseAuthException    If the user could not be verified
     */
    public Map<String, Object> getUserData(String idToken) throws DatabaseAccessException, FirebaseAuthException {
        // Verify the ID token and retrieve the UID
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String userId = decodedToken.getUid(); // Extract UID from the token

        // Use the UID to fetch user data
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(userId);

        try {
            return docRef.get().get().getData();
        } catch (Exception e) {
            LoggerUtil.logException("Error retrieving data for user: " + userId, e);
            throw new DatabaseAccessException("Error accessing data for user: " + userId);
        }
    }

    /**
     * Fetches server constants from a specified JSON file located at a remote server.
     * The URL of the JSON file is constructed using the `DATABASE_URL` constant.
     *
     * @return a {@link JsonObject} containing the server constants if the request is successful.
     */
    private static JsonObject fetchServerConstants() {
        try {
            URI uri = URI.create(DATABASE_URL + "serverConstants.json");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return gson.fromJson(response.toString(), JsonObject.class);
            } else {
                throw new Exception("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            LoggerUtil.logException("Error fetching server constants", e);
            System.exit(1);
        }

        return null;
    }
}
