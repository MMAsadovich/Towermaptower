package com.example.testactivity.ui.signal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SignalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is signal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}