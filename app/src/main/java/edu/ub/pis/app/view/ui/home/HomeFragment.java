package edu.ub.pis.app.view.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentHomeBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        Context currentContext = root.getContext();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

                        ArrayList<Routine> routines = new ArrayList<>();
                        ArrayList<Integer> nRoutines = new ArrayList<>();
                        ArrayList<Integer> nExercises = new ArrayList<>();
                        final ArrayList<String> daysList = new ArrayList<>();
                        daysList.add("Monday");
                        daysList.add("Tuesday");
                        daysList.add("Wednesday");
                        daysList.add("Thursday");
                        daysList.add("Friday");
                        daysList.add("Saturday");
                        daysList.add("Sunday");
                        for(int i = 0; i < 7; i++) {
                            nRoutines.add(0);
                            nExercises.add(0);
                        }

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String routineName = document.getId(); // Get the document name
                            List<Map<String, Object>> exercisesList = (List<Map<String, Object>>) document.get("exercises");
                            ArrayList<Boolean> days = (ArrayList<Boolean>) document.get("days");

                            ArrayList<Exercise> exercisesArray = new ArrayList<>();

                            for (Map<String, Object> exercise : exercisesList) {
                                String name = (String) exercise.get("name");
                                String weight = (String) exercise.get("weight");
                                String series = (String) exercise.get("series");
                                String reps = (String) exercise.get("reps");
                                String pr = (String) exercise.get("pr");
                                ArrayList<Boolean> completed = (ArrayList<Boolean>) exercise.get("completed");
                                //Log.println(Log.ASSERT, name,""+completed.size());
                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), reps, Toast.LENGTH_SHORT);
                                toast.show();*/

                                exercisesArray.add(new Exercise(name, series, reps, weight, pr, completed));
                            }

                            routines.add(new Routine(routineName, exercisesArray, days));
                        }


                        LocalDate currentDate = LocalDate.now();
                        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
                        int currentDayOfWeekValue = currentDayOfWeek.getValue() - 1;

                        ArrayList<Routine> todayRoutines = new ArrayList<>();

                        for(Routine r : routines) {
                            if(r.getDays().get(currentDayOfWeekValue)) {
                                todayRoutines.add(r);
                            }
                        }

                        if(todayRoutines.size() > 0) {
                            TextView textView = root.findViewById(R.id.textView4);
                            textView.setVisibility(View.GONE);
                        }

                        RecyclerView recyclerView = root.findViewById(R.id.recylcerView2);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set the layout manager
                        RoutineStatisticAdapter adapter = new RoutineStatisticAdapter(todayRoutines, getContext(), currentDayOfWeekValue); // Pass your data list and context
                        recyclerView.setAdapter(adapter);

                        for(Routine r : routines) {
                            int i = 0;
                            for(boolean day : r.getDays()) {
                                if(day) {
                                    int current = nRoutines.get(i)+1;
                                    nRoutines.set(i, current);
                                    int current2 = nExercises.get(i)+r.getExercises().size();
                                    nExercises.set(i, current2);
                                }
                                i++;
                            }
                        }

                        TableLayout tableLayout = root.findViewById(R.id.table_layout);

                        for (int i = 0; i < 7; i++) {
                            TableRow dataRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
                            ((TextView) dataRow.findViewById(R.id.column1)).setText(daysList.get(i));
                            ((TextView) dataRow.findViewById(R.id.column2)).setText(nRoutines.get(i) + " routines");
                            ((TextView) dataRow.findViewById(R.id.column3)).setText(nExercises.get(i) + " exercises");
                            if(i % 2 == 0) {
                                ((TextView) dataRow.findViewById(R.id.column1)).setBackgroundColor(getResources().getColor(R.color.green_yellow));
                                ((TextView) dataRow.findViewById(R.id.column2)).setBackgroundColor(getResources().getColor(R.color.green_yellow));
                                ((TextView) dataRow.findViewById(R.id.column3)).setBackgroundColor(getResources().getColor(R.color.green_yellow));
                            }
                            tableLayout.addView(dataRow);
                        }

                    }
                }
            });
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}