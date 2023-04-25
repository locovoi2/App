package edu.ub.pis.app.view.ui.trainers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TrainersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}