package edu.ub.pis.app.view.ui.manage_routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.model.RoutineRepository;

public class ManageRoutinesFragment extends Fragment {
    private FragmentManageRoutinesBinding binding;
    private final String TAG = "SignInActivity";
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual
    private ManageRoutinesViewModel mManageRoutinesViewModel;
    private EditText mEditTextRoutineName;
    private EditText mEditTextExerciseName;
    private EditText mEditTextSeries;
    private EditText mEditTextReps;
    private EditText mEditTextWeight;
    private Button mButtonAdd;
    private Button mButtonFinish;
    private RoutineRepository mRepository;
    private ArrayList<Exercise> exes;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageRoutinesBinding.inflate(inflater, container, false);

        //Nuestro viewModel
        mManageRoutinesViewModel = new ViewModelProvider(this).get(ManageRoutinesViewModel.class);
        mRepository = RoutineRepository.getInstance();

        exes = new ArrayList<>();

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
                    mEditTextWeight.getText().toString());
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
        } else if(exes.isEmpty()) {
            Toast.makeText(getContext(), "Minimum 1", Toast.LENGTH_SHORT).show();
        } else {
            Routine routine = new Routine(name, exes);
            mRepository.addRoutine(userEmail, routine);
            exes.clear(); //Buidem array
            mEditTextRoutineName.setText("");
        }
    }

    protected void addExercise(String name, String series, String reps, String weight) {
        if(name.isEmpty() || series.isEmpty() || reps.isEmpty() || weight.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            Exercise exercise = new Exercise(name, series, reps, weight);
            exes.add(exercise);
            System.out.println(exes);
            mEditTextExerciseName.setText("");
            mEditTextSeries.setText("");
            mEditTextReps.setText("");
            mEditTextWeight.setText("");
        }
    }
}