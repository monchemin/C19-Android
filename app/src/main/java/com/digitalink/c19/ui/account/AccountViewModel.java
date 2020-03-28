package com.digitalink.c19.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.LocationsPresenter;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class AccountViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<LocationsPresenter>> locations() {
        Call<BasePresenter<LocationsPresenter>> call = api.locations();
        return getData(call);
    }
}