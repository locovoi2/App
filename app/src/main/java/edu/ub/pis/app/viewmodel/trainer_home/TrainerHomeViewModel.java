package edu.ub.pis.app.viewmodel.trainer_home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import edu.ub.pis.app.model.User;
import edu.ub.pis.app.model.UserRepository;

public class TrainerHomeViewModel extends AndroidViewModel {
    private final String TAG = "HomeActivityViewModel";

    /* Elements observables del ViewModel */
    private final MutableLiveData<User> mUser; //User actual

    /* Repositori (base de dades) dels usuaris */
    private UserRepository mUserRepository; // On es manté la informació dels usuaris

    /* Atributs auxiliars */
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //mail del usuario actual
    private FirebaseStorage mStorage;

    public TrainerHomeViewModel(Application application) {
        super(application);

        // Instancia els atributs
        mUser = new MutableLiveData<>();
        mUserRepository = UserRepository.getInstance();

        // Quan s'acabi de llegir de la BBDD l'usuaris, el ViewModel ha d'actualitzar
        // l'observable mUsers.
        mUserRepository.addOnLoadUserListener(new UserRepository.OnLoadUserListener() {
            @Override
            public void onLoadUser(User user) {
                setUser(user);
            }
        });
    }

    /*
     * Retorna els usuaris perquè la HomeActivity pugui subscriure-hi l'observable.
     */
    public LiveData<User> getUser() {
        return mUser;
    }

    public boolean getPremium() {
        return mUser.getValue().getPremium();
    }

    public void updatePremium(boolean premium) {
        mUserRepository.UpdatePremiumUser(userEmail, premium);
        loadUserFromRepository();
    }

    /*
     * Mètode que serà invocat pel UserRepository.OnLoadUsersListener definit al
     * constructor (quan l'objecte UserRepository hagi acabat de llegir de la BBDD).
     */
    public void setUser(User user) {
        mUser.setValue(user);
    }

    public void loadUserFromRepository() {
        mUserRepository.loadUser(userEmail);
    }
    public void loadUserFromRepository(String mail) {
        mUserRepository.loadUser(mail);
    }
}
