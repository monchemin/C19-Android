package com.digitalink.c19.presenter;

import com.google.gson.annotations.SerializedName;

public class CountryPresenter extends PresenterFactory {
    @SerializedName("id")
    public String ID;

    @SerializedName("name")
    public String name;
}
