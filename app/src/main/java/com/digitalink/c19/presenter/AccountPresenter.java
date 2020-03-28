package com.digitalink.c19.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountPresenter {
    @SerializedName("district_id")
    public String ID;

    @SerializedName("gender")
    public String gender;

    @SerializedName("age")
    public int age;

    @SerializedName("weight")
    public double weight;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("is_diabetic")
    public boolean is_diabetic;

    @SerializedName("is_hypertensive")
    public boolean is_hypertensive;

    @SerializedName("is_asthmatic")
    public boolean is_asthmatic;

    @SerializedName("is_cardio_ischemic")
    public boolean is_cardio_ischemic;

    @SerializedName("has_lung_disease")
    public boolean has_lung_disease;

    @SerializedName("has_kidney_disease")
    public boolean has_kidney_disease;

    @SerializedName("is_smoker")
    public boolean is_smoker;

    @SerializedName("is_return_from_travel")
    public boolean is_return_from_travel;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("latitude")
    public double latitude;

    public JsonObject toJson() {
        String jsonInString = new Gson().toJson(this);
        return new Gson().fromJson(jsonInString, JsonObject.class);
    }
}
