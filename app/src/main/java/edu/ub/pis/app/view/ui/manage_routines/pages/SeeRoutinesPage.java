package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.databinding.PageSeeRoutinesBinding;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.viewmodel.manage_routines.pages.SeeRoutinesViewModel;


public class SeeRoutinesPage extends Fragment {
    private PageSeeRoutinesBinding binding;

    /** ViewModel del SeeRoutinesPage */
    private SeeRoutinesViewModel mSeeRoutinesViewModel;

    private RecyclerView mRoutineCardsRV; //RecyclerView

    private RoutineCardAdapter mRoutineCardRVAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.page_see_routines,
                        container,
                        false);

        binding = PageSeeRoutinesBinding.inflate(inflater, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicialitza el ViewModel d'aquest fragment (SeeRoutinesPage)
        mSeeRoutinesViewModel = new ViewModelProvider(this)
                .get(SeeRoutinesViewModel.class);

        mRoutineCardsRV = binding.seeRoutinesRv;

        //Assignem un layout manager.
        LinearLayoutManager manager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false
        );
        mRoutineCardsRV.setLayoutManager(manager);

        // (2) Inicialitza el RecyclerViewAdapter i li assignem a la RecyclerView.
        mRoutineCardRVAdapter = new RoutineCardAdapter(
                mSeeRoutinesViewModel.getRoutines().getValue() // Passem-li referencia llista rutines
        );
        mRoutineCardRVAdapter.setOnClickInfoListener(new RoutineCardAdapter.OnClickInfoListener() {
            // Listener que escoltarà quan interactuem amb un item en una posició donada
            // dins de la recicler view. En aquest cas, quan es faci clic a l'imatge d'info
            @Override
            public void OnClickInfo(int position) {
                //codi per veure els exercicis
                System.out.println("InfoListener funciona");
            }
        });

        mRoutineCardsRV.setAdapter(mRoutineCardRVAdapter); //Associem adapter

        // Observer a HomeActivity per veure si la llista de User (observable MutableLiveData)
        // a HomeActivityViewModel ha canviat.
        final Observer<ArrayList<Routine>> observerRoutines = new Observer<ArrayList<Routine>>() {
            @Override
            public void onChanged(ArrayList<Routine> users) {
                mRoutineCardRVAdapter.notifyDataSetChanged();
            }
        };
        mSeeRoutinesViewModel.getRoutines().observe(getViewLifecycleOwner(), observerRoutines);

        // A partir d'aquí, en cas que es faci cap canvi a la llista d'usuaris, HomeActivity ho sabrá
        mSeeRoutinesViewModel.loadUsersFromRepository();  // Internament pobla els usuaris de la BBDD
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
