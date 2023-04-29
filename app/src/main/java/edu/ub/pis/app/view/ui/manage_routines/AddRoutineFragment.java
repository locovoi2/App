package edu.ub.pis.app.view.ui.manage_routines;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentAddNewRoutineBinding;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.RoutineRepository;

public class AddRoutineFragment extends Fragment {
    private FragmentAddNewRoutineBinding binding;
    private final String TAG = "SignInActivity";
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual
    private ManageRoutinesViewModel mManageRoutinesViewModel;
    private RecyclerView exercisesRecyclerView;
    private EditText mEditTextRoutineName;
    private EditText mEditTextExerciseName;
    private EditText mEditTextSeries;
    private EditText mEditTextReps;
    private EditText mEditTextWeight;
    private Button mButtonAdd;
    private Button mButtonFinish;
    private RoutineRepository mRepository;
    private ArrayList<Exercise> exercisesList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddNewRoutineBinding.inflate(inflater, container, false);

        exercisesRecyclerView = binding.exercisesRecyclerView;
        //exerciseNameEditText = view.findViewById(R.id.exercise_name_edit_text);
        //addExerciseButton = view.findViewById(R.id.add_exercise_button);

        // Set up the RecyclerView
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisesList = new ArrayList<>(); // Initialize your list of exercises here
        ExercisesAdapter exercisesAdapter = new ExercisesAdapter(exercisesList);
        exercisesRecyclerView.setAdapter(exercisesAdapter);

        exercisesList = new ArrayList<>();
        //Inicializar vistas
        mEditTextRoutineName = binding.editTextRoutineName;
        mEditTextExerciseName =  binding.editTextExerciseName;
        mEditTextSeries = binding.editTextSeries;
        mEditTextReps = binding.editTextReps;
        mEditTextWeight = binding.editTextWeight;
        mButtonAdd = binding.buttonAdd;
        mButtonFinish = binding.buttonFinish;

        return view;
    }

}
}
