package edu.ub.pis.app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;

public class RoutinesActivity extends AppCompatActivity {

    private RecyclerView mUserCardsRV;
    private UserCardAdapter mUserCardRVAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        ArrayList<Routine> routines = new ArrayList<Routine>();

        Context currentContext = this;

        if(currentUser != null) {
            String userEmail = currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");
            DocumentReference userDocRef = usersRef.document(userEmail);
            CollectionReference routinesRef = userDocRef.collection("routines");

            routinesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String routineName = document.getId(); // Get the document name
                            List<Map<String, Object>> exercisesList = (List<Map<String, Object>>) document.get("exercises");

                            ArrayList<Exercise> exercisesArray = new ArrayList<>();

                            for(Map<String, Object> exercise : exercisesList) {
                                String name = (String) exercise.get("name");
                                String weight = (String) exercise.get("weight");
                                String series = (String) exercise.get("series");
                                String reps = (String) exercise.get("reps");
                                boolean completed = (boolean) exercise.get("completed");
/*
                                Toast toast = Toast.makeText(getApplicationContext(), reps, Toast.LENGTH_SHORT);
                                toast.show();*/

                                exercisesArray.add(new Exercise(name, series, reps, weight, completed));
                            }

                            routines.add(new Routine(routineName, exercisesArray));
                        }

                        Toast toast = Toast.makeText(getApplicationContext(), ""+routines.size(), Toast.LENGTH_SHORT);
                        toast.show();

                        // Anem a buscar la RecyclerView i fem dues coses:
                        mUserCardsRV = findViewById(R.id.userCardRv);

                        // (1) Li assignem un layout manager.
                        LinearLayoutManager manager = new LinearLayoutManager(
                                currentContext, LinearLayoutManager.VERTICAL, false
                        );
                        mUserCardsRV.setLayoutManager(manager);

                        mUserCardRVAdapter = new UserCardAdapter(
                                routines, currentContext // Passem-li referencia llista usuaris
                        );

                        mUserCardRVAdapter.setOnClickHideListener(new UserCardAdapter.OnClickHideListener() {
                            // Listener que escoltarà quan interactuem amb un item en una posició donada
                            // dins de la recicler view. En aquest cas, quan es faci clic al botó d'amagar
                            // l'usuari.
                            @Override
                            public void OnClickHide(int position) {
                                mUserCardRVAdapter.hideUser(position);
                            }
                        });

                        mUserCardRVAdapter.setOnClickPictureListener(new UserCardAdapter.OnClickPictureListener() {
                            @Override
                            public void onClickPicture(String name) {
                                Toast toast = Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        mUserCardsRV.setAdapter(mUserCardRVAdapter);

                    } else {
                        //Error
                        Toast toast = Toast.makeText(getApplicationContext(), "Error al buscar els documents", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }


    }
}

