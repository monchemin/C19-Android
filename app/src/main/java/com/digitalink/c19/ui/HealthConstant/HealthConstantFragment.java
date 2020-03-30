package com.digitalink.c19.ui.HealthConstant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.HealthConstantPresenter;
import com.digitalink.c19.utils.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class HealthConstantFragment extends Fragment {

    private HealthConstantViewModel healthConstantViewModel;
    private TextInputEditText temperature;
    private HealthConstantPresenter presenter = new HealthConstantPresenter();
    private ScrollView rootLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthConstantViewModel = ViewModelProviders.of(this).get(HealthConstantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health_constant, container, false);
        rootLayout = root.findViewById(R.id.scrollView);
        temperature = root.findViewById(R.id.temperature);

        SwitchCompat is_tired = root.findViewById(R.id.is_tired);
        is_tired.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.is_tired = isChecked);

        SwitchCompat has_dry_cough = root.findViewById(R.id.has_dry_cough);
        has_dry_cough.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_dry_cough = isChecked);

        SwitchCompat has_shortness_of_breath = root.findViewById(R.id.has_shortness_of_breath);
        has_shortness_of_breath.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_shortness_of_breath = isChecked);

        SwitchCompat has_been_in_contact_with_infected_person = root.findViewById(R.id.has_been_in_contact_with_infected_person);
        has_been_in_contact_with_infected_person.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_been_in_contact_with_infected_person = isChecked);

        SwitchCompat has_headache = root.findViewById(R.id.has_headache);
        has_headache.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_headache = isChecked);

        SwitchCompat has_runny_nose = root.findViewById(R.id.has_runny_nose);
        has_runny_nose.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_runny_nose = isChecked);

        SwitchCompat has_nasal_congestion = root.findViewById(R.id.has_nasal_congestion);
        has_nasal_congestion.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_nasal_congestion = isChecked);

        SwitchCompat has_sore_throat = root.findViewById(R.id.has_sore_throat);
        has_sore_throat.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_sore_throat = isChecked);

        SwitchCompat has_muscle_pain = root.findViewById(R.id.has_muscle_pain);
        has_muscle_pain.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_muscle_pain = isChecked);

        SwitchCompat has_diarrhea = root.findViewById(R.id.has_diarrhea);
        has_diarrhea.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.has_diarrhea = isChecked);
        MaterialButton button = root.findViewById(R.id.btn_send_info);
        button.setOnClickListener(v -> validateAndSend());
        return root;
    }

    private void validateAndSend() {
        String ID = Preference.GetID(getContext());
        if (ID.equals("")) {
           makeSnackBar(getString(R.string.connected_before));
            return;
        }
        presenter.patientID = ID;
        try {
            presenter.temperature = Float.parseFloat(temperature.getText().toString());
        } catch (NumberFormatException e) {
            temperature.setError(getString(R.string.missing_field));
            return;
        }

        healthConstantViewModel.addHealthConstant(presenter.toJson()).observe(this, result -> {
            if (result == null) {
                makeSnackBar(getString(R.string.info_sent_error));
                return;
            }
            if (result.ID == null) {
                makeSnackBar(getString(R.string.info_sent_error));
            } else {
                makeSnackBar(getString(R.string.info_sent_success));
            }
        });
    }

    private void makeSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(rootLayout, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.close, v -> {
            snackbar.dismiss();
        });
        snackbar.show();
    }
}
