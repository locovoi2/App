package edu.ub.pis.app.view.ui.manage_routines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageRoutinesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManageRoutinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}