package com.digitalink.c19.presenter;

import com.google.gson.annotations.SerializedName;

public class LoginPresenter extends PresenterFactory {
    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("id")
    public String ID;
}
