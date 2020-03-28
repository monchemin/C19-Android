package com.digitalink.c19.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class AccountViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<LocalizationPresenter>> locations() {
        Call<BasePresenter<LocalizationPresenter>> call = api.locations();
        return getData(call);
    }

    public MutableLiveData<BasePresenter<String>> addPatient(JsonObject data) {
        Call<BasePresenter<String>> call = api.addPatient(data);
        return getData(call);
    }
}