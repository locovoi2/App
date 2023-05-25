package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import edu.ub.pis.app.databinding.PageAddNewRoutineBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.model.RoutineRepository;
import edu.ub.pis.app.viewmodel.manage_routines.pages.SeeRoutinesViewModel;


public class AddRoutinesPage extends Fragment {
    private PageAddNewRoutineBinding binding;
    private SeeRoutinesViewModel mSeeRoutinesViewModel;
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

        binding = PageAddNewRoutineBinding.inflate(inflater, container, false);

        //Inicialitzem view model de see routines per poder repintar el RV de rutines guardades
        //ViewModelProvider ja segueix singleton
        mSeeRoutinesViewModel = new ViewModelProvider(this)
                .get(SeeRoutinesViewModel.class);

        exercisesRecyclerView = binding.exercisesRecyclerView;

        // Set up the RecyclerView
        exercisesList = new ArrayList<>();// Initialize your list of exercises here

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
            mSeeRoutinesViewModel.loadRoutinesFromRepository();
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
            Exercise exercise = new Exercise(name, series, reps, weight, "0");
            exercisesList.add(exercise);
            mEditTextExerciseName.setText("");
            mEditTextSeries.setText("");
            mEditTextReps.setText("");
            mEditTextWeight.setText("");
            exercisesAdapter.updateExercises();
            exercisesAdapter.setExercises(exercisesList);
        }
    }


}
