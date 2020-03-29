package com.digitalink.c19.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class PresenterFactory {

    public JsonObject toJson() {
        String jsonInString = new Gson().toJson(this);
        return new Gson().fromJson(jsonInString, JsonObject.class);
    }
}
