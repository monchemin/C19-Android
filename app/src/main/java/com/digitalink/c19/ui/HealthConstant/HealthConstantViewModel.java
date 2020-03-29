package com.digitalink.c19.ui.HealthConstant;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class HealthConstantViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<Void>> addHealthConstant(JsonObject data) {
        Call<BasePresenter<Void>> call = api.addHealthConstant(data);
        return getData(call);
    }
}