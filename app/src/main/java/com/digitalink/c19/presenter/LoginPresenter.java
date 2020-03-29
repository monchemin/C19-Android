package com.digitalink.c19.presenter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginPresenter extends PresenterFactory {
    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("id")
    public String ID;

    @SerializedName("daily_information")
    public List<HealthConstantPresenter> dailyInformation;
}
