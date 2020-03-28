package com.digitalink.c19.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.base.BaseViewModel;
import com.digitalink.c19.presenter.LocalizationPresenter;

import retrofit2.Call;

public class AccountViewModel extends BaseViewModel {

    public MutableLiveData<BasePresenter<LocalizationPresenter>> locations() {
        Call<BasePresenter<LocalizationPresenter>> call = api.locations();
        return getData(call);
    }
}