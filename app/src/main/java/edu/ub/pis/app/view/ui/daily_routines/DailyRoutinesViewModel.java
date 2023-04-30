package edu.ub.pis.app.view.ui.daily_routines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailyRoutinesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DailyRoutinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hola que tal");
    }

    public LiveData<String> getText() {
        return mText;
    }
}