package edu.ub.pis.app.viewmodel.trainers;

        import android.app.Application;

        import androidx.lifecycle.AndroidViewModel;
        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;

        import com.google.firebase.auth.FirebaseAuth;

        import java.util.ArrayList;

        import edu.ub.pis.app.model.User;
        import edu.ub.pis.app.model.UserRepository;

public class SeeTrainersViewModel extends AndroidViewModel {
    private final String TAG = "SeeTrainersViewModel";

    /* Elements observables del ViewModel */
    private final MutableLiveData<ArrayList<User>> mUsers; // Els trainers que la RecyclerView mostra
    private final MutableLiveData<Integer> mHidPosition;

    /* Repositori (base de dades) dels usuaris */
    private UserRepository mUserRepository; // On es manté la informació dels usuaris

    /* Atributs auxiliars */
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual

    public SeeTrainersViewModel(Application application) {
        super(application);

        // Instancia els atributs
        mUsers = new MutableLiveData<>(new ArrayList<>());
        mHidPosition = new MutableLiveData<>();
        mUserRepository = UserRepository.getInstance();

        // Quan s'acabin de llegir de la BBDD els usuaris, el ViewModel ha d'actualitzar
        // l'observable mUsers. I com que la RecyclerView de la HomeActivity està observant aquesta
        // mateixa variable (mUsers), també se n'enterarà
        mUserRepository.addOnLoadUsersListener(new UserRepository.OnLoadUsersListener() {
            @Override
            public void onLoadUsers(ArrayList<User> users) {
                setUsers(users);
            }
        });
    }

    /*
     * Retorna els usuaris perquè la HomeActivity pugui subscriure-hi l'observable.
     */
    public LiveData<ArrayList<User>> getUsers() {
        return mUsers;
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
    public void setUsers(ArrayList<User> users) {
        mUsers.setValue(users);
    }

    public void loadUsersFromRepository() {
        mUserRepository.loadUsers(mUsers.getValue());
    }



}