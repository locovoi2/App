package edu.ub.pis.app.view.ui.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentReportsBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Agafar dades de la base

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Routine> routines = new ArrayList<>();

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
                            ArrayList<Boolean> days = (ArrayList<Boolean>) document.get("days");

                            ArrayList<Exercise> exercisesArray = new ArrayList<>();

                            for (Map<String, Object> exercise : exercisesList) {
                                String name = (String) exercise.get("name");
                                String weight = (String) exercise.get("weight");
                                String series = (String) exercise.get("series");
                                String reps = (String) exercise.get("reps");
                                ArrayList<Boolean> completed = (ArrayList<Boolean>) exercise.get("completed");
                                //Log.println(Log.ASSERT, name,""+completed.size());
                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), reps, Toast.LENGTH_SHORT);
                                toast.show();*/

                                exercisesArray.add(new Exercise(name, series, reps, weight, completed));
                            }

                            routines.add(new Routine(routineName, exercisesArray, days));
                        }


                        LocalDate currentDate = LocalDate.now();
                        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
                        int currentDayOfWeekValue = currentDayOfWeek.getValue() - 1;

                        int nToComplete = 0;
                        int nCompleted = 0;
                        for(Routine r : routines) {
                            if(r.getDays().get(currentDayOfWeekValue)) {
                                for(Exercise e : r.getExercises()) {
                                    if(e.getCompleted().get(currentDayOfWeekValue)) {
                                        nCompleted++;
                                    }
                                    else
                                        nToComplete++;
                                }
                            }
                        }

                        // Mostrar els charts
                        PieChart pieChart = root.findViewById(R.id.pieChart);

                        List<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(nToComplete, "To do"));
                        entries.add(new PieEntry(nCompleted, "Completed"));

                        PieDataSet pieDataSet = new PieDataSet(entries, "");
                        pieDataSet.setColors(new int[]{Color.LTGRAY, Color.DKGRAY}); // Customize colors if needed
                        pieDataSet.setValueTextSize(16f);
                        pieDataSet.setValueTextColor(R.color.dark_green);

                        PieData pieData = new PieData(pieDataSet);

                        pieChart.setData(pieData);
                        pieChart.invalidate(); // Refresh the chart
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setHoleColor(Color.TRANSPARENT);

                        pieChart.getDescription().setEnabled(false);
                        Legend legend = pieChart.getLegend();
                        legend.setTextSize(16f);

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