package com.digitalink.c19.api;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.presenter.CountryPresenter;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.digitalink.c19.presenter.LoginPresenter;
import com.digitalink.c19.utils.Configuration;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class Service {

    private static Retrofit retrofit = null;
    private static String BASE_API = Configuration.apiHost();

    public static EndPoint Api() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_API)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit.create(EndPoint.class);
    }

    public interface EndPoint {
        @GET("/position/localizations")
        Call<BasePresenter<List<LocalizationPresenter>>> locations();

        @GET("/position/countries")
        Call<BasePresenter<List<CountryPresenter>>> countries();

        @POST("/patient/add")
        Call<BasePresenter<Void>> addPatient(@Body JsonObject data);

        @POST("/patient/constant/add")
        Call<BasePresenter<Void>> addHealthConstant(@Body JsonObject data);

        @POST("/patient/connect")
        Call<BasePresenter<LoginPresenter>> connection(@Body JsonObject data);
    }
}
