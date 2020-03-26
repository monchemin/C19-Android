package com.digitalink.c19.ui.HealthConstant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HealthConstantViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HealthConstantViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is constant fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}