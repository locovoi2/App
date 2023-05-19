package edu.ub.pis.app.model;



import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
 * Segueix el patró de disseny Singleton.
 */
public class UserRepository {
    private static final String TAG = "Repository";

    /** Autoinstància, pel patró singleton */
    private static final UserRepository mInstance = new UserRepository();

    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;

    /** Definició de listener (interficie),
     *  per escoltar quan s'hagin acabat de llegir els usuaris de la BBDD */
    public interface OnLoadUsersListener {
        void onLoadUsers(ArrayList<User> users);
    }

    public interface OnLoadUserListener {
        void onLoadUser(User users);
    }

    public ArrayList<OnLoadUsersListener> mOnLoadUsersListeners = new ArrayList<>();

    public ArrayList<OnLoadUserListener> mOnLoadUserListeners = new ArrayList<>();

    /** Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret */
    public interface OnLoadUserPictureUrlListener {
        void OnLoadUserPictureUrl(String pictureUrl);
    }

    public OnLoadUserPictureUrlListener mOnLoadUserPictureUrlListener;

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */
    private UserRepository() {
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Retorna aqusta instancia singleton
     * @return
     */
    public static UserRepository getInstance() {
        return mInstance;
    }

    /**
     * Afegir un listener de la operació OnLoadUsersListener.
     * Pot haver-n'hi només un. Fem llista, com a exemple, per demostrar la flexibilitat
     * d'aquest disseny.
     * @param listener
     */
    public void addOnLoadUsersListener(OnLoadUsersListener listener) {
        mOnLoadUsersListeners.add(listener);
    }

    public void addOnLoadUserListener(OnLoadUserListener listener) {
        mOnLoadUserListeners.add(listener);
    }

    /**
     * Setejem un listener de la operació OnLoadUserPictureUrlListener.
     * En aquest cas, no és una llista de listeners. Només deixem haver-n'hi un,
     * també a tall d'exemple.
     * @param listener
     */
    public void setOnLoadUserPictureListener(OnLoadUserPictureUrlListener listener) {
        mOnLoadUserPictureUrlListener = listener;
    }

    /**
     * Mètode que llegeix els usuaris. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadUsers.
     */
    public void loadUsers(ArrayList<User> users){
        users.clear();
        mDb.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int usCode;
                                if(document.getLong("user_code") != null) {
                                    usCode = document.getLong("user_code").intValue();
                                }
                                else usCode = 0;

                                boolean usPremium;
                                if(document.getBoolean("user_premium") != null) {
                                    usPremium = document.getBoolean("user_premium");
                                }
                                else usPremium = false;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = new User(
                                        document.toString(), // ID = Email
                                        document.getString("name"),
                                        document.getString("surname"),
                                        document.getBoolean("trainer"),
                                        document.getString("description"),
                                        document.getString("price"),
                                        usCode,
                                        document.getString("contact_phone_number"),
                                        usPremium
                                );
                                users.add(user);
                            }
                            /* Callback listeners */
                            for (OnLoadUsersListener l: mOnLoadUsersListeners) {
                                l.onLoadUsers(users);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Mètode que llegeix un unic usuari. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadUser.
     */
    public void loadUser(String email){
        mDb.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            System.out.println(document);
                            int usCode;
                            if(document.getLong("user_code") != null) {
                                usCode = document.getLong("user_code").intValue();
                            }
                            else usCode = 0;

                            boolean usPremium;
                            if(document.getBoolean("user_premium") != null) {
                                usPremium = document.getBoolean("user_premium");
                            }
                            else usPremium = false;

                            Log.d(TAG, document.getId() + " => " + document.getData());
                            User user = new User(
                                    document.toString(), // ID = Email
                                    document.getString("name"),
                                    document.getString("surname"),
                                    document.getBoolean("trainer"),
                                    document.getString("description"),
                                    document.getString("price"),
                                    usCode,
                                    document.getString("contact_phone_number"),
                                    usPremium
                            );
                            /* Callback listeners */
                            for (OnLoadUserListener l: mOnLoadUserListeners) {
                                l.onLoadUser(user);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Mètode que llegeix la Url d'una foto de perfil d'un usuari indicat pel seu
     * email. Vindrà cridat des de fora i quan acabi, avisarà sempre al listener,
     * invocant el seu OnLoadUserPictureUrl.
     */
    public void loadPictureOfUser(String email) {
        mDb.collection("users")
                .document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                mOnLoadUserPictureUrlListener.OnLoadUserPictureUrl(document.getString("picture_url"));
                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Mètode que afegeix un nou usuari a la base de dades. Utilitzat per la funció
     * de Sign-Up (registre) de la SignUpActivity.
     * @param email
     * @param name
     * @param surname
     * @param trainer
     * @param price
     * @param contactPhoneNumber
     * @param userCode
     * @param description
     * @param userPremium
     */
    public void addUser(
            String name,
            String surname,
            String email,
            boolean trainer,
            String price,
            String contactPhoneNumber,
            int userCode,
            String description,
            boolean userPremium
    ) {
        // Obtenir informació personal de l'usuari
        Map<String, Object> signedUpUser = new HashMap<>();
        signedUpUser.put("name", name);
        signedUpUser.put("surname", surname);
        signedUpUser.put("trainer", trainer);
        signedUpUser.put("price", price);
        signedUpUser.put("contact_phone_number", contactPhoneNumber);
        signedUpUser.put("user_code", userCode);
        signedUpUser.put("description", description);
        signedUpUser.put("user_premium", userPremium);

        // Afegir-la a la base de dades
        mDb.collection("users").document(email).set(signedUpUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sign up completion succeeded");
                        } else {
                            Log.d(TAG, "Sign up completion failed");
                        }
                    }
                });
    }

    /**
     * Actualiza si el user es premium o no, sera usado para implementar el boton de vip
     * @param email
     * @param userPremium
     */
    public void UpdatePremiumUser(
            String email,
            boolean userPremium
    ) {
        // Obtenir informació personal de l'usuari
        Map<String, Object> actualizar = new HashMap<>();
        actualizar.put("user_premium", userPremium);

        // Afegir-la a la base de dades
        mDb.collection("users").document(email).update(actualizar)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sign up completion succeeded");
                        } else {
                            Log.d(TAG, "Sign up completion failed");
                        }
                    }
                });
    }

    /**
     * Mètode que guarda la Url d'una foto de perfil que un usuari hagi pujat
     * des de la HomeActivity a la BBDD. Concretament, es cridat pel HomeActivityViewModel.
     * @param userId
     * @param pictureUrl
     */
    public void setPictureUrlOfUser(String userId, String pictureUrl) {
        Map<String, Object> userEntry = new HashMap<>();
        userEntry.put("picture_url", pictureUrl);

        mDb.collection("users")
                .document(userId)
                .set(userEntry, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Photo upload succeeded: " + pictureUrl);
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Photo upload failed: " + pictureUrl);
                });
    }
}
