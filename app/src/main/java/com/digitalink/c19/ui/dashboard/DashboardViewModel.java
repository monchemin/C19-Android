package com.digitalink.c19.ui.dashboard;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.CountryPresenter;
import com.digitalink.c19.presenter.LoginPresenter;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;

public class DashboardViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<LoginPresenter>> connection(JsonObject data) {
        Call<BasePresenter<LoginPresenter>> call = api.connection(data);
        return getData(call);
    }

    public MutableLiveData<BasePresenter<List<CountryPresenter>>> getCountries() {
        Call<BasePresenter<List<CountryPresenter>>> call = api.countries();
        return getData(call);
    }
}