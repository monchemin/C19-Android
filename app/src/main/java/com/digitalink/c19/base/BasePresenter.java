package com.digitalink.c19.base;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BasePresenter<T> {

    @SerializedName("ID")
    public String ID;

    @SerializedName("error")
    public String error;

    @SerializedName("response")
    public List<T> response = new ArrayList<>();

}
