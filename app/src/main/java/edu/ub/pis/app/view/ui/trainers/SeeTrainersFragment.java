package edu.ub.pis.app.view.ui.trainers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentTrainersBinding;
import edu.ub.pis.app.databinding.PageSeeRoutinesBinding;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.model.User;
import edu.ub.pis.app.view.ui.manage_routines.pages.RoutineCardAdapter;
import edu.ub.pis.app.viewmodel.SeeUsersViewModel;
import edu.ub.pis.app.viewmodel.manage_routines.pages.SeeRoutinesViewModel;

public class SeeTrainersFragment extends Fragment {

    private FragmentTrainersBinding binding;
    private SeeUsersViewModel mSeeTrainersViewModel;

    private RecyclerView mTrainersRV; //RecyclerView

    private TrainerCardAdapter mTrainerCardRVAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_trainers,
                        container,
                        false);

        binding = FragmentTrainersBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicialitza el ViewModel d'aquest fragment
        mSeeTrainersViewModel = new ViewModelProvider(this)
                .get(SeeUsersViewModel.class);

        mTrainersRV = binding.seeTrainersRV;

        Log.d("Missatge", "HOLAAA ENTRO A onViewCreated");

        //Assignem un layout manager.
        LinearLayoutManager manager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false
        );
        manager.setInitialPrefetchItemCount(3);
        mTrainersRV.setLayoutManager(manager);

        //Log.d("Missatge 2", "Mida users:" + mSeeTrainersViewModel.getUsers().getValue().size());

        //Triem els users que son Trainers
        ArrayList<User> trainers = new ArrayList<>();
        for(User user : mSeeTrainersViewModel.getUsers().getValue()){
            if(user.getTrainer()){
                trainers.add(user);
            }
        }

        // (2) Inicialitza el RecyclerViewAdapter i li assignem a la RecyclerView.
        mTrainerCardRVAdapter = new TrainerCardAdapter(getContext(),
                trainers // Passem-li referencia llista trainers
        );

        mTrainersRV.setAdapter(mTrainerCardRVAdapter); //Associem adapter

        mTrainerCardRVAdapter.setOnClickInfoListener(new TrainerCardAdapter.OnClickInfoListener() {
            // Listener que escoltarà quan interactuem amb un item en una posició donada
            // dins de la recicler view. En aquest cas, quan es faci clic a l'imatge d'info
            @Override
            public void OnClickInfo(int position) {
                //codi per veure els exercicis
                Toast.makeText(getContext(), "Este boton funciona", Toast.LENGTH_SHORT).show();
            }
        });

        mSeeTrainersViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                // Triem els usuaris que son Trainers
                ArrayList<User> trainers = new ArrayList<>();
                for(User user : users){
                    if(user.getTrainer()){
                        trainers.add(user);
                    }
                }

                // Actualitzem la llista d'entrenadors en el RecyclerViewAdapter
                mTrainerCardRVAdapter.updateTrainers(trainers);
            }
        });

// A partir d'aquí, en cas que es faci cap canvi a la llista d'usuaris, HomeActivity ho sabrá
        mSeeTrainersViewModel.loadUsersFromRepository();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


