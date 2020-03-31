package com.digitalink.c19.ui.history;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.LoginPresenter;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class HistoryViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<LoginPresenter>> connection(JsonObject data) {
        Call<BasePresenter<LoginPresenter>> call = api.connection(data);
        return getData(call);
    }

}