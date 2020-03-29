package com.digitalink.c19.base;

import com.google.gson.annotations.SerializedName;

public class BasePresenter<T> {

    @SerializedName("ID")
    public String ID;

    @SerializedName("error")
    public String error;

    @SerializedName("response")
    public T response;

}
