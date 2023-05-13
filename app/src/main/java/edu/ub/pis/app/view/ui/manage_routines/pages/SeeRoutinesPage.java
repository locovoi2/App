package edu.ub.pis.app.view.ui.manage_routines.pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.ub.pis.app.R;
import edu.ub.pis.app.databinding.FragmentManageRoutinesBinding;
import edu.ub.pis.app.databinding.PageSeeRoutinesBinding;
import edu.ub.pis.app.model.Exercise;
import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.viewmodel.manage_routines.pages.SeeRoutinesViewModel;


public class SeeRoutinesPage extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
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

        return binding.getRoot();
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
        manager.setInitialPrefetchItemCount(3);
        mRoutineCardsRV.setLayoutManager(manager);

        // (2) Inicialitza el RecyclerViewAdapter i li assignem a la RecyclerView.
        mRoutineCardRVAdapter = new RoutineCardAdapter(getContext(),
                mSeeRoutinesViewModel.getRoutines().getValue() // Passem-li referencia llista rutines
        );

        mRoutineCardsRV.setAdapter(mRoutineCardRVAdapter); //Associem adapter

        //Associem ItemTouchHelper que ajudi a deslliçar
        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, SeeRoutinesPage.this);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRoutineCardsRV);

        mRoutineCardRVAdapter.setOnClickInfoListener(new RoutineCardAdapter.OnClickInfoListener() {
            // Listener que escoltarà quan interactuem amb un item en una posició donada
            // dins de la recicler view. En aquest cas, quan es faci clic a l'imatge d'info
            @Override
            public void OnClickInfo(int position) {
                //codi per veure els exercicis
                Toast.makeText(getContext(), "Este boton funciona", Toast.LENGTH_SHORT).show();
            }
        });

        // Observer a HomeActivity per veure si la llista de User (observable MutableLiveData)
        // a HomeActivityViewModel ha canviat.

        final Observer<ArrayList<Routine>> observerRoutines = new Observer<ArrayList<Routine>>() {
            @Override
            public void onChanged(ArrayList<Routine> Routines) {
                mRoutineCardRVAdapter.updateRoutines();
            }
        };
        mSeeRoutinesViewModel.getRoutines().observe(getViewLifecycleOwner(), observerRoutines);

        // A partir d'aquí, en cas que es faci cap canvi a la llista d'usuaris, HomeActivity ho sabrá
        mSeeRoutinesViewModel.loadRoutinesFromRepository();  // Internament pobla les rutines de la BBDD

    }

    //Listener per quan es deslliça
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof RoutineCardAdapter.ViewHolder) {
            mSeeRoutinesViewModel.removeRoutineFromSeeRoutines(position);
            mRoutineCardRVAdapter.hideRoutine(position);

            //TextView que conte el nom de la rutina
            TextView RoutineName = viewHolder.itemView.findViewById(R.id.routine_name);

            Snackbar snackbar = Snackbar.make(((RoutineCardAdapter.ViewHolder) viewHolder).mlayoutABorrar,
                    RoutineName.getText() + " Deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSeeRoutinesViewModel.addRoutineFromSeeRoutines(position);
                    mRoutineCardRVAdapter.restoreItem(position);
                }
            });

            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar1, int event) {
                    if(event != DISMISS_EVENT_ACTION) {
                        //Si la snackbar es tanca sense accio, es a dir,
                        //Despres de que s'hagi acabat el temps per poder desfer la eliminacio
                        //Es confirma l'eliminacio a la BBDD
                        mSeeRoutinesViewModel.removeRoutineFromBBDD();
                    }
                }
            });

            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
