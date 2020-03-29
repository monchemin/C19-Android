package com.digitalink.c19.presenter;

import com.digitalink.c19.R;
import com.digitalink.c19.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

public class HealthConstantPresenter extends PresenterFactory {
    @SerializedName("patient_id")
    public String patientID;

    @SerializedName("date_time")
    public String dateTime;

    @SerializedName("temperature")
    public float temperature;

    @SerializedName("is_tired")
    public boolean is_tired;

    @SerializedName("has_dry_cough")
    public boolean has_dry_cough;

    @SerializedName("has_shortness_of_breath")
    public boolean has_shortness_of_breath;

    @SerializedName("has_been_in_contact_with_infected_person")
    public boolean has_been_in_contact_with_infected_person;

    @SerializedName("has_headache")
    public boolean has_headache;

    @SerializedName("has_runny_nose")
    public boolean has_runny_nose;

    @SerializedName("has_nasal_congestion")
    public boolean has_nasal_congestion;

    @SerializedName("has_sore_throat")
    public boolean has_sore_throat;

    @SerializedName("has_muscle_pain")
    public boolean has_muscle_pain;

    @SerializedName("has_diarrhea")
    public boolean has_diarrhea;

    public String formatDate(String format) {
        return  DateUtils.dateToString(DateUtils.fromISO8601UTC(dateTime), format);
    }
}
