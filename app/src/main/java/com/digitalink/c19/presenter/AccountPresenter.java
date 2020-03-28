package com.digitalink.c19.presenter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountPresenter {
    @SerializedName("id")
    public String ID;

    @SerializedName("age")
    public int age;

    @SerializedName("weight")
    public double weight;

    @SerializedName("phone_number")
    public String phoneNumber;

    public JSONObject toJson() {
        String jsonInString = new Gson().toJson(this);
        try {
            return new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
