package edu.ub.pis.app.viewmodel.manage_routines.pages;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.ub.pis.app.model.Routine;
import edu.ub.pis.app.model.RoutineRepository;

public class SeeRoutinesViewModel extends AndroidViewModel {
    private final String TAG = "SeeRoutineViewModel";

    /* Elements observables del ViewModel */
    private final MutableLiveData<ArrayList<Routine>> mRoutines; // Les rutines que la RecyclerView mostra

    /* Repositori (base de dades) dels usuaris */
    private RoutineRepository mRoutineRepository; // On es manté la informació dels usuaris

    /* Atributs auxiliars */
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual
    private MutableLiveData<Routine> RoutineToDelete;

    public SeeRoutinesViewModel(Application application) {
        super(application);

        // Instancia els atributs
        mRoutines = new MutableLiveData<>(new ArrayList<>());
        mRoutineRepository = RoutineRepository.getInstance();
        RoutineToDelete = new MutableLiveData<>();

        // Quan s'acabin de llegir de la BBDD les rutines, el ViewModel ha d'actualitzar
        // l'observable mRoutines. I com que la RecyclerView de la SeeRoutinesPage està observant aquesta
        // mateixa variable (mRoutines), també se n'enterarà
        mRoutineRepository.addOnLoadRoutinesListener(new RoutineRepository.OnLoadRoutinesListener() {
            @Override
            public void onLoadRoutines(ArrayList<Routine> routines) {
                setRoutines(routines);
            }
        });
    }

    /*
     * Retorna els usuaris perquè la HomeActivity pugui subscriure-hi l'observable.
     */
    public LiveData<ArrayList<Routine>> getRoutines() {
        return mRoutines;
    }

    /*
     * Mètode que serà invocat pel RoutineRepository.OnLoadRoutinesListener definit al
     * constructor (quan l'objecte RoutineRepository hagi acabat de llegir de la BBDD).
     */
    public void setRoutines(ArrayList<Routine> routines) {
        mRoutines.setValue(routines);
    }

    /* Mètode que crida a carregar dades dels usuaris */
    public void loadRoutinesFromRepository() {
        mRoutineRepository.loadRoutines(userEmail, mRoutines.getValue());
    }

    /*
     * Mètode que esborra una rutina de la llista de rutinas, donada una posició en
     * la llista. La posició ve del RoutineCardAdapter, que es torna a la SeeRoutinesPage
     * i aquesta crida aquest mètode, després que el SeeRoutinesViewModel hagi esborrat
     * la rutina en qüestió de mRoutines.
     * Es guarda la rutina per si es vol recuperar o per confirmar l'eliminacio
     */
    public void removeRoutineFromSeeRoutines(int position) {
        //Guardem la rutina per si vol desfer l'eliminacio
        RoutineToDelete.setValue(mRoutines.getValue().get(position));

        mRoutines.getValue().remove(position);
    }

    /*
     * Mètode que recupera la rutina que es volia borrar de la llista de rutinas, donada una posició en
     * la llista. La posició ve del RoutineCardAdapter, que es torna a la SeeRoutinesPage
     * i aquesta crida aquest mètode, després que el SeeRoutinesViewModel hagi esborrat
     * la rutina en qüestió de mRoutines.
     */
    public void addRoutineFromSeeRoutines(int position) {
        mRoutines.getValue().add(position, RoutineToDelete.getValue());
    }

    public void removeRoutineFromBBDD() {
        mRoutineRepository.deleteRoutine(userEmail, RoutineToDelete.getValue().getName());
    }
}
