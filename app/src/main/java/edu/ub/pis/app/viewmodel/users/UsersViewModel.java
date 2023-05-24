package edu.ub.pis.app.viewmodel.users;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import edu.ub.pis.app.model.User;
import edu.ub.pis.app.model.UserMailFirebase;
import edu.ub.pis.app.model.UserRepository;

public class UsersViewModel extends AndroidViewModel {
    private final String TAG = "UsersViewModel";

    private final MutableLiveData<ArrayList<User>> mUsers;

    private final MutableLiveData<ArrayList<UserMailFirebase>> mUsersTrained;
    private UserRepository mUserRepository; // On es manté la informació dels usuaris

    /*mUserRepository.addOnLoadUsersListener(new UserRepository.OnLoadUsersListener() {
        @Override
        public void onLoadUsers(ArrayList<User> users) {
            setUsers(users);
        }
    });*/
    public UsersViewModel(Application application) {
        super(application);
        mUsers = new MutableLiveData<>(new ArrayList<>());
        mUsersTrained = new MutableLiveData<>(new ArrayList<>());
        mUserRepository = UserRepository.getInstance();
        mUserRepository.addOnLoadUsersListener(new UserRepository.OnLoadUsersListener() {
            @Override
            public void onLoadUsers(ArrayList<User> users) {
                setUsers(users);
            }
        });

        mUserRepository.addOnLoadUsersTrainedListener(new UserRepository.OnLoadUsersTrainedListener() {
            @Override
            public void onLoadUsersTrained(ArrayList<UserMailFirebase> usersTrained) {
                setUsersTrained(usersTrained);
            }
        });
    }

    public void loadUsersFromRepository() { mUserRepository.loadUsers(mUsers.getValue()); }

    public LiveData<ArrayList<User>> getUsers() { return mUsers; }
    public LiveData<ArrayList<UserMailFirebase>> getUsersTrained() { return mUsersTrained; }

    public void setUsers(ArrayList<User> users) {
        mUsers.setValue(users);
    }
    public void setUsersTrained(ArrayList<UserMailFirebase> users) {
        mUsersTrained.setValue(users);
    }

    public void addUserTrained(String email, String user) {mUserRepository.addUserTrained(email, user);}

    public void loadUsersTrained(String email) {mUserRepository.loadUsersTrained(email, mUsersTrained.getValue());}
}