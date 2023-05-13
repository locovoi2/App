package edu.ub.pis.app.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Classe que conecta la BBDD amb el model (de rutines) amb singleton
 */
public class RoutineRepository {
    private static final String TAG = "RoutineRepository";

    /** Autoinstància, pel patró singleton */
    private static final RoutineRepository mInstance = new RoutineRepository();

    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;

    /** Definició de listener (interficie),
     *  per escoltar quan s'hagin acabat de llegir els usuaris de la BBDD */
    public interface OnLoadRoutinesListener {
        void onLoadRoutines(ArrayList<Routine> users);
    }

    public ArrayList<OnLoadRoutinesListener> mOnLoadRoutinesListeners = new ArrayList<>();

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */
    private RoutineRepository() { mDb = FirebaseFirestore.getInstance(); }


    /**
     * Retorna aquesta instancia singleton
     * @return
     */
    public static RoutineRepository getInstance() { return mInstance; }

    public void addOnLoadRoutinesListener(OnLoadRoutinesListener listener) {
        mOnLoadRoutinesListeners.add(listener);
    }

    /**
     * Mètode que afeigeix una rutina a la BBDD
     * @param email
     * @param routine
     */
    public void addRoutine (String email, Routine routine) {
        mDb.collection("users")
                .document(email)
                .collection("routines")
                .document(routine.getName())
                .set(routine)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Routine saved");
                        } else {
                            Log.d(TAG, "Routine not saved");
                        }
                    }
                });
    }

    /**
     * Metode que elimina una rutina completa de la BBDD
     * @param email
     * @param routineName
     */
    public void deleteRoutine (String email, String routineName) {
        mDb.collection("users")
                .document(email)
                .collection("routines")
                .document(routineName)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Routine deleted");
                        } else {
                            Log.d(TAG, "Routine not deleted");
                        }
                    }
                });
    }

    /**
     * Mètode que llegeix els usuaris. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadUsers.
     */
    public void loadRoutines(String email, ArrayList<Routine> routines){
        routines.clear();
        mDb.collection("users")
                .document(email)
                .collection("routines")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Routine routine = document.toObject(Routine.class);
                                routines.add(routine);
                            }
                            /* Callback listeners */
                            for (OnLoadRoutinesListener l: mOnLoadRoutinesListeners) {
                                l.onLoadRoutines(routines);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
