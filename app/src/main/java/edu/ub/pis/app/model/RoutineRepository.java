package edu.ub.pis.app.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Classe que conecta la BBDD amb el model (de rutines) amb singleton
 */
public class RoutineRepository {
    private static final String TAG = "RoutineRepository";

    /** Autoinstància, pel patró singleton */
    private static final RoutineRepository mInstance = new RoutineRepository();

    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;

    private RoutineRepository() { mDb = FirebaseFirestore.getInstance(); }

    public static RoutineRepository getInstance() { return mInstance; }

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
}
