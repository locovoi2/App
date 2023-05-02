package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentAddNewRoutineBinding;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.model.RoutineRepository;
import edu.ub.pis.app.model.UserRepository;
import edu.ub.pis.app.view.ui.manage_routines.ExercisesAdapter;


public class AddRoutinesPage extends Fragment {
    private FragmentAddNewRoutineBinding binding;
    private final String TAG = "SignInActivity";
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual
    private RecyclerView exercisesRecyclerView;
    private EditText mEditTextRoutineName;
    private EditText mEditTextExerciseName;
    private EditText mEditTextSeries;
    private EditText mEditTextReps;
    private EditText mEditTextWeight;
    private Button mButtonAdd;
    private Button mButtonFinish;
    private RoutineRepository mRepository = RoutineRepository.getInstance();;
    private ArrayList<Exercise> exercisesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewRoutineBinding.inflate(inflater, container, false);

        exercisesRecyclerView = binding.exercisesRecyclerView;
        //exerciseNameEditText = view.findViewById(R.id.exercise_name_edit_text);
        //addExerciseButton = view.findViewById(R.id.add_exercise_button);

        // Set up the RecyclerView
        exercisesList = new ArrayList<>();// Initialize your list of exercises here
        Exercise provaEx = new Exercise("Curl", "4", "10", "12");
        exercisesList.add(provaEx);
        Exercise provaEx2 = new Exercise("Curl 2", "4", "10", "12");
        exercisesList.add(provaEx2);
        Exercise provaEx3 = new Exercise("Curl3", "4", "10", "12");
        exercisesList.add(provaEx3);
        Exercise provaEx4 = new Exercise("Curl4", "4", "10", "12");
        exercisesList.add(provaEx4);

        LinearLayoutManager manager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false
        );
        manager.setInitialPrefetchItemCount(3);
        exercisesRecyclerView.setLayoutManager(manager);

        ExercisesAdapter exercisesAdapter = new ExercisesAdapter(exercisesList);
        exercisesRecyclerView.setAdapter(exercisesAdapter);


        //Inicializar vistas
        mEditTextRoutineName = binding.editTextRoutineName;
        mEditTextExerciseName =  binding.editTextExerciseName;
        mEditTextSeries = binding.editTextSeries;
        mEditTextReps = binding.editTextReps;
        mEditTextWeight = binding.editTextWeight;
        mButtonAdd = binding.buttonAdd;
        mButtonFinish = binding.buttonFinish;

        mButtonAdd.setOnClickListener(view -> {
            addExercise(mEditTextExerciseName.getText().toString(),
                    mEditTextSeries.getText().toString(),
                    mEditTextReps.getText().toString(),
                    mEditTextWeight.getText().toString(),
                    exercisesAdapter);

        });

        mButtonFinish.setOnClickListener(view -> {
            addRoutine(mEditTextRoutineName.getText().toString());
        });
        return binding.getRoot();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void addRoutine(String name) {
        if(name.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else if(exercisesList.isEmpty()) {
            Toast.makeText(getContext(), "Minimum 1", Toast.LENGTH_SHORT).show();
        } else {
            Routine routine = new Routine(name, exercisesList);
            mRepository.addRoutine(userEmail, routine);
            exercisesList.clear(); //Buidem array
            mEditTextRoutineName.setText("");
        }
    }

    protected void addExercise(String name, String series, String reps, String weight, ExercisesAdapter exercisesAdapter) {
        if(name.isEmpty() || series.isEmpty() || reps.isEmpty() || weight.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            Exercise exercise = new Exercise(name, series, reps, weight);
            exercisesList.add(exercise);
            System.out.println(exercisesList);
            mEditTextExerciseName.setText("");
            mEditTextSeries.setText("");
            mEditTextReps.setText("");
            mEditTextWeight.setText("");
            exercisesAdapter.updateExercises();
            exercisesAdapter.setExercises(exercisesList);
        }
    }


}
