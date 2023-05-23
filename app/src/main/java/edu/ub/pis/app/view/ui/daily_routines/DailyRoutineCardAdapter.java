package edu.ub.pis.app.view.ui.daily_routines;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.view.UserCardAdapter;
import edu.ub.pis.app.view.ui.daily_routines.pages.DailyPage;

public class DailyRoutineCardAdapter extends RecyclerView.Adapter<DailyRoutineCardAdapter.ViewHolder> {

    private ArrayList<Routine> dataSet;
    private int day;
    private UserCardAdapter userCardAdapter;
    private DailyPage parent;

    public DailyRoutineCardAdapter(ArrayList<Routine> dataSet, int day, UserCardAdapter userCardAdapter, DailyPage parent) {
        this.dataSet = dataSet;
        this.day = day;
        this.userCardAdapter = userCardAdapter;
        this.parent = parent;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView routineNameTextView;
        public Button addButton;
        public Button removeButton;

        public ViewHolder(View view) {
            super(view);
            routineNameTextView = view.findViewById(R.id.exercicename);
            addButton = view.findViewById(R.id.dailyAdd);
            removeButton = view.findViewById(R.id.dailyRemove);
            // Add other views from your card layout as needed
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_routine_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int p) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Routine cardData = dataSet.get(p);
        String userEmail = mAuth.getCurrentUser().getEmail();

        // Bind data to the views in your card layout
        holder.routineNameTextView.setText(cardData.getName());
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFirestoreArray(userEmail, day, cardData, true, null);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Exercise> newExercises = new ArrayList<>();
                for(Routine r : dataSet) {
                    if(r.getName().equals(dataSet.get(p).getName())) {
                        for(Exercise e : r.getExercises()) {
                            ArrayList<Boolean> es = e.getCompleted();
                            es.set(day,false);
                            e.setCompleted(es);
                            newExercises.add(e);
                        }
                    }
                }
                updateFirestoreArray(userEmail, day, cardData, false, newExercises);
            }
        });

    }

    private void updateFirestoreArray(String userEmail, int position, Routine cardData, boolean isToday, ArrayList<Exercise> exercises) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        DocumentReference userDocRef = usersRef.document(userEmail);
        CollectionReference routinesRef = userDocRef.collection("routines");

        routinesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot totalRoutines = task.getResult();

                    for (QueryDocumentSnapshot document : totalRoutines) {

                        String routineName = document.getId();

                        if (routineName.equals(cardData.getName())) {
                            ArrayList<Boolean> days = (ArrayList<Boolean>) document.get("days");


                            //Log.println(Log.ASSERT, updatedRoutine.getName(), ""+updatedRoutine.getExercises().get(0).getName());
                            Log.println(Log.ASSERT, ""+position, ""+isToday);
                            days.set(position, isToday);
                            // Update here
                            ArrayList<Routine> totalNewRoutines = new ArrayList<>();
                            for (Routine r : dataSet) {
                                if(r.getName().equals(routineName)) {
                                    r.setDay(days);
                                }
                                totalNewRoutines.add(r);
                            }

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("days", days);
                            if(exercises != null)
                                updateData.put("exercises", exercises);

                            DocumentReference routineDocRef = routinesRef.document(document.getId());
                            routineDocRef.update(updateData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            parent.updateRoutines(totalNewRoutines);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
