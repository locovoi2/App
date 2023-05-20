package edu.ub.pis.app.view.ui.daily_routines;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ub.pis.app.R;
import edu.ub.pis.app.model.Routine;

public class DailyRoutineCardAdapter extends RecyclerView.Adapter<DailyRoutineCardAdapter.ViewHolder> {

    private ArrayList<Routine> dataSet;
    private int day;

    public DailyRoutineCardAdapter(ArrayList<Routine> dataSet, int day) {
        this.dataSet = dataSet;
        this.day = day;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int p) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Routine cardData = dataSet.get(p);
        String userEmail = mAuth.getCurrentUser().getEmail();

        // Bind data to the views in your card layout
        holder.routineNameTextView.setText(cardData.getName());
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFirestoreArray(userEmail, day, cardData, true);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFirestoreArray(userEmail, day, cardData, false);
            }
        });

    }

    private void updateFirestoreArray(String userEmail, int position, Routine cardData, boolean isToday) {
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
                            Log.println(Log.ASSERT, ""+position, ""+isToday);
                            days.set(position, isToday);
                            // Update here
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("days", days);

                            DocumentReference routineDocRef = routinesRef.document(document.getId());
                            routineDocRef.update(updateData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Falta actualitzar al moment
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
