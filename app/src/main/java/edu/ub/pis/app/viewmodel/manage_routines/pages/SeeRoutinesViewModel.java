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
    private final MutableLiveData<Integer> mHidPosition;

    /* Repositori (base de dades) dels usuaris */
    private RoutineRepository mRoutineRepository; // On es manté la informació dels usuaris

    /* Atributs auxiliars */

    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual

    public SeeRoutinesViewModel(Application application) {
        super(application);

        // Instancia els atributs
        mRoutines = new MutableLiveData<>(new ArrayList<>());
        mHidPosition = new MutableLiveData<>();
        mRoutineRepository = RoutineRepository.getInstance();

        // Quan s'acabin de llegir de la BBDD els usuaris, el ViewModel ha d'actualitzar
        // l'observable mUsers. I com que la RecyclerView de la HomeActivity està observant aquesta
        // mateixa variable (mUsers), també se n'enterarà
        mRoutineRepository.addOnLoadUsersListener(new RoutineRepository.OnLoadRoutinesListener() {
            @Override
            public void onLoadRoutines(ArrayList<Routine> routines) {
                SeeRoutinesViewModel.this.setRoutines(routines);
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
     * Retorna el LiveData de la URL de la foto per a què HomeActivity
     * pugui subscriure-hi l'observable.
     */
    public LiveData<Integer> getHidPosition() {
        return mHidPosition;
    }

    /*
     * Mètode que serà invocat pel UserRepository.OnLoadUsersListener definit al
     * constructor (quan l'objecte UserRepository hagi acabat de llegir de la BBDD).
     */
    public void setRoutines(ArrayList<Routine> routines) {
        mRoutines.setValue(routines);
    }

    /* Mètode que crida a carregar dades dels usuaris */
    public void loadUsersFromRepository() {
        mRoutineRepository.loadRoutines(userEmail, mRoutines.getValue());
    }

    /*
     * Mètode que esborra un usuari de la llista d'usuaris, donada una posició en
     * la llista. La posició ve del UserCardAdapter, que es torna a la HomeActivity
     * i aquesta crida aquest mètode, després que el HomeActivityViewModel hagi esborrat
     * l'usuari en qüestió de mUsers.
     */
    public void removeUserFromHome(int position) {
        mRoutines.getValue().remove(position);
    }
}
