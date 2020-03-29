package com.digitalink.c19.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;

public class AccountViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<List<LocalizationPresenter>>> locations() {
        Call<BasePresenter<List<LocalizationPresenter>>> call = api.locations();
        return getData(call);
    }

    public MutableLiveData<BasePresenter<Void>> addPatient(JsonObject data) {
        Call<BasePresenter<Void>> call = api.addPatient(data);
        return getData(call);
    }
}