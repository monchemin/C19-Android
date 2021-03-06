package com.digitalink.c19.presenter;

import com.google.gson.annotations.SerializedName;

public class LocalizationPresenter extends PresenterFactory {

    @SerializedName("id")
    public String id;

    @SerializedName("position")
    public String position;

    @SerializedName("country")
    public String country;

}
