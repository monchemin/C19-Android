package com.digitalink.c19.ui.history;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.HealthConstantPresenter;


public class HistoryDialog extends DialogFragment {

    private HealthConstantPresenter presenter;



    public HistoryDialog(HealthConstantPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_history_dialog, container, false);

        (root.findViewById(R.id.button_close)).setOnClickListener(v -> dismiss());

        TextView temperature = root.findViewById(R.id.temperature);
        String temp = getString(R.string.temperature) + ": " + presenter.temperature;
        temperature.setText(temp);

        TextView dateTime = root.findViewById(R.id.date_time);
        String dateT = "Date: " + presenter.formatDate(getString(R.string.date_format2));
        dateTime.setText(dateT);

        SwitchCompat is_tired = root.findViewById(R.id.is_tired);
       is_tired.setChecked(presenter.is_tired);

        SwitchCompat has_dry_cough = root.findViewById(R.id.has_dry_cough);
        has_dry_cough.setChecked(presenter.has_dry_cough);

        SwitchCompat has_shortness_of_breath = root.findViewById(R.id.has_shortness_of_breath);
        has_shortness_of_breath.setChecked(presenter.has_shortness_of_breath);

        SwitchCompat has_been_in_contact_with_infected_person = root.findViewById(R.id.has_been_in_contact_with_infected_person);
        has_been_in_contact_with_infected_person.setChecked(presenter.has_been_in_contact_with_infected_person);

        SwitchCompat has_headache = root.findViewById(R.id.has_headache);
        has_headache.setChecked(presenter.has_headache);

        SwitchCompat has_runny_nose = root.findViewById(R.id.has_runny_nose);
        has_runny_nose.setChecked(presenter.has_runny_nose);

        SwitchCompat has_nasal_congestion = root.findViewById(R.id.has_nasal_congestion);
        has_nasal_congestion.setChecked(presenter.has_nasal_congestion);

        SwitchCompat has_sore_throat = root.findViewById(R.id.has_sore_throat);
        has_sore_throat.setChecked(presenter.has_sore_throat);

        SwitchCompat has_muscle_pain = root.findViewById(R.id.has_muscle_pain);
        has_muscle_pain.setChecked(presenter.has_muscle_pain);

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
