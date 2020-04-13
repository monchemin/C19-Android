package com.digitalink.c19.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalink.c19.api.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseViewModel extends ViewModel {

    protected Service.EndPoint api = Service.Api();

    protected <T> MutableLiveData<BasePresenter<T>> getData(Call<BasePresenter<T>> call) {
        final MutableLiveData<BasePresenter<T>> data = new MutableLiveData<>();
        call.enqueue(new Callback<BasePresenter<T>>() {
            @Override
            public void onResponse(Call<BasePresenter<T>> call, Response<BasePresenter<T>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BasePresenter<T>> call, Throwable t) {
                t.printStackTrace();
                BasePresenter<T> err = new BasePresenter<>();
                data.setValue(err);
            }
        });
        return data;
    }
}
