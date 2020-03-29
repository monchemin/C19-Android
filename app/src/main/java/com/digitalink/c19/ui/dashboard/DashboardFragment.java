package com.digitalink.c19.ui.dashboard;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.ConnectionPresenter;
import com.digitalink.c19.presenter.CountryPresenter;
import com.digitalink.c19.utils.Preference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private List<CountryPresenter> countryPresenters = new ArrayList<>();
    private AppCompatSpinner spinner;
    private ConnectionPresenter connectionPresenter = new ConnectionPresenter();
    private String selectedCountry;
    private TextView dashboardInfo;
    private TextInputEditText phoneNumber;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        spinner = root.findViewById(R.id.country_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        phoneNumber = root.findViewById(R.id.phone_number);
        dashboardInfo = root.findViewById(R.id.dashboard_info);
        MaterialButton button = root.findViewById(R.id.btn_login);
        button.setOnClickListener(v -> validateAndSend());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardViewModel.getCountries().observe(this, result -> {
            if (result != null) {
                countryPresenters = result.response;
                fillCountrySpinner();
            }
        });
    }

    private void fillCountrySpinner() {
        List<String> list = new ArrayList<>();
        selectedCountry = "code";
        list.add(selectedCountry);
        for (CountryPresenter c : countryPresenters) {
            list.add(c.ID);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void validateAndSend() {
        if (selectedCountry.equals("code")) {
            dashboardInfo.setText("Missing code");
            return;
        }
        String phone = phoneNumber.getText().toString();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneNumber.setError("phone");
            return;
        }

        connectionPresenter.phoneNumber = selectedCountry + phone;


        dashboardViewModel.connection(connectionPresenter.toJson()).observe(this, result -> {
            if (result == null) {
                dashboardInfo.setText("error occur");
                return;
            }
            if (result.response == null) {
                Toast.makeText(getContext(), "error occur one", Toast.LENGTH_LONG).show();
                return;
            }
            if (result.response.ID == null) {
                Toast.makeText(getContext(), "error occur two", Toast.LENGTH_LONG).show();
                return;
            }

            dashboardInfo.setText(getString(R.string.success_connection));
            Preference.setActive(getContext(), result.response.ID, result.response.phoneNumber);
        });
    }
}
