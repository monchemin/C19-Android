package com.digitalink.c19.api;

import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.presenter.LocationsPresenter;
import com.digitalink.c19.utils.Configuration;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

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
        Call<BasePresenter<LocationsPresenter>> locations();
    }
}
