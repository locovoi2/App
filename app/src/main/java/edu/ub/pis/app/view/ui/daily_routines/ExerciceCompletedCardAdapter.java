package edu.ub.pis.app.view.ui.daily_routines;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.view.UserCardAdapter;
import edu.ub.pis.app.view.ui.daily_routines.pages.DailyPage;

public class ExerciceCompletedCardAdapter extends RecyclerView.Adapter<ExerciceCompletedCardAdapter.ViewHolder> {

    private ArrayList<Exercise> cardItems;
    private int day;
    private Context context;
    private String routineName;
    private UserCardAdapter recycleCardAdapter;
    private DailyPage parent;

    public ExerciceCompletedCardAdapter(ArrayList<Exercise> cardItems, Context context, int day, String routineName, UserCardAdapter recycleCardAdapter, DailyPage parent) {
        this.cardItems = cardItems;
        this.context = context;
        this.day = day;
        this.routineName = routineName;
        this.recycleCardAdapter = recycleCardAdapter;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercice_completed_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise cardItem = cardItems.get(position);
        holder.exercicename.setText(cardItem.getName());
        ArrayList<Boolean> completedDays = cardItem.getCompleted();
        holder.completedBox.setChecked(completedDays.get(day));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();

        holder.completedBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            completedDays.set(day, isChecked);
            cardItem.setCompleted(completedDays);

            updateFirestoreArray(userEmail, day, cardItem, completedDays, routineName);

        });
    }

    private void updateFirestoreArray(String userEmail, int position, Exercise cardData, ArrayList<Boolean> completedDays, String routineName) {
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

                        String routineNameCurrent = document.getId();

                        if (routineName.equals(routineNameCurrent)) {
                            Routine routineToChange =  document.toObject(Routine.class);
                            ArrayList<Exercise> exercises = routineToChange.getExercises();
                            for(Exercise exercise : exercises) {
                                if(exercise.getName().equals(cardData.getName())) {
                                    exercise.setCompleted(completedDays);
                                    document.getReference().update("exercises", exercises)
                                            .addOnSuccessListener(aVoid -> {
                                                // Successfully updated the Firestore document
                                                ArrayList<Routine> newRoutines = new ArrayList<>();
                                                for(Routine r : recycleCardAdapter.getUsers()) {
                                                    if(r.getName().equals(routineName)) {
                                                        ArrayList<Exercise> newExercises = new ArrayList<>();
                                                        for(Exercise e : r.getExercises()) {
                                                            if(e.getName().equals(exercise.getName())) {
                                                                e.setCompleted(completedDays);
                                                            }
                                                            newExercises.add(e);
                                                        }
                                                        r.setExercises(newExercises);
                                                    }
                                                    newRoutines.add(r);
                                                }
                                                parent.updateRoutines(newRoutines);
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle the failure to update the Firestore document
                                            });
                                }
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIcon;
        TextView exercicename;
        CheckBox completedBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarIcon = itemView.findViewById(R.id.avatarIcon);
            exercicename = itemView.findViewById(R.id.exercicename);
            completedBox = itemView.findViewById(R.id.completedBox);
        }
    }
}
