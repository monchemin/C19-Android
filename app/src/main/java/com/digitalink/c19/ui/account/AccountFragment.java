package com.digitalink.c19.ui.account;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.AccountPresenter;
import com.digitalink.c19.presenter.CountryPresenter;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.digitalink.c19.ui.ActionChooseListener;
import com.digitalink.c19.utils.Preference;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountFragment extends Fragment implements ActionChooseListener {

    private AccountViewModel accountViewModel;
    private List<LocalizationPresenter> presenters;
    private TextInputEditText localization, phoneNumber, age, weight, height;
    private AccountPresenter accountPresenter = new AccountPresenter();
    private LocalizationPresenter localizationPresenter;
    private List<CountryPresenter> countryPresenters;
    private AppCompatSpinner spinner;
    private String selectedCountry;
    private ScrollView rootLayout;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        rootLayout = root.findViewById(R.id.scrollView);

        localization = root.findViewById(R.id.localization);
        phoneNumber = root.findViewById(R.id.phone_number);
        age = root.findViewById(R.id.age);
        weight = root.findViewById(R.id.weight);
        height = root.findViewById(R.id.height);

        SwitchCompat is_diabetic = root.findViewById(R.id.is_diabetic);
        is_diabetic.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_diabetic = isChecked);

        SwitchCompat is_hypertensive = root.findViewById(R.id.is_hypertensive);
        is_hypertensive.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_hypertensive = isChecked);

        SwitchCompat is_asthmatic = root.findViewById(R.id.is_asthmatic);
        is_asthmatic.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_asthmatic = isChecked);

        SwitchCompat is_cardio_ischemic = root.findViewById(R.id.is_cardio_ischemic);
        is_cardio_ischemic.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_cardio_ischemic = isChecked);

        SwitchCompat has_lung_disease = root.findViewById(R.id.has_lung_disease);
        has_lung_disease.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.has_lung_disease = isChecked);

        SwitchCompat has_kidney_disease = root.findViewById(R.id.has_kidney_disease);
        has_kidney_disease.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.has_kidney_disease = isChecked);

        SwitchCompat is_smoker = root.findViewById(R.id.is_smoker);
        is_smoker.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_smoker = isChecked);

        SwitchCompat is_return_from_travel = root.findViewById(R.id.is_return_from_travel);
        is_return_from_travel.setOnCheckedChangeListener((buttonView, isChecked) -> accountPresenter.is_return_from_travel = isChecked);

        AppCompatSpinner gspinner = root.findViewById(R.id.gender_spinner);
        gspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accountPresenter.gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MaterialButton button = root.findViewById(R.id.btn_create_account);
        localization.setOnClickListener(v -> {
            if (presenters != null) {
                showDialog();
            }
        });

        localization.setOnFocusChangeListener((v, hasFocus) -> {
            if (presenters != null && hasFocus) {
                showDialog();
            }
        });

        button.setOnClickListener(v -> validate());
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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenters == null) {
            accountViewModel.locations().observe(getViewLifecycleOwner(), results -> {
                if (results != null && results.response != null) {
                    presenters = results.response;
                } else {
                    makeSnackBar(getString(R.string.connection_error));
                }
            });
        }

        if (countryPresenters == null) {
            accountViewModel.getCountries().observe(this, result -> {
                if (result != null && result.response != null) {
                    countryPresenters = result.response;
                    fillCountrySpinner();
                }
            });
        }
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialog() {

        if (selectedCountry.equals("code")) {
            makeSnackBar(getString(R.string.missing_code));
            return;
        }
        List<LocalizationPresenter> selected = presenters.stream().filter(c -> c.country.equals(selectedCountry)).collect(Collectors.toList());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        LocalizationSearchDialog newFragment = new LocalizationSearchDialog(selected);
        newFragment.setListener(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    @Override
    public void sendInput(String input) {
        localization.setText(null);
        for (LocalizationPresenter presenter : presenters) {
            if (presenter.id.equals(input)) {
                localization.setText(presenter.position);
                localizationPresenter = presenter;
                break;
            }
        }

    }

    private void validate() {
        if (selectedCountry.equals("code")) {
            phoneNumber.setError(getString(R.string.missing_code));
            return;
        }

        String phone = phoneNumber.getText().toString();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneNumber.setError(getString(R.string.missing_field));
            return;
        }

        if (localizationPresenter == null || localization.getText() == null) {
            localization.setError(getString(R.string.missing_field));
            return;
        }

        try {
            accountPresenter.age = Integer.parseInt(age.getText().toString());
        } catch (NumberFormatException e) {
            age.setError(getString(R.string.missing_field));
            return;
        }

        try {
            accountPresenter.weight = Double.parseDouble(weight.getText().toString());
        } catch (NumberFormatException e) {
            weight.setError(getString(R.string.missing_field));
            return;
        }

        try {
            accountPresenter.height = Integer.parseInt(height.getText().toString());
        } catch (NumberFormatException e) {
            height.setError(getString(R.string.missing_field));
            return;
        }

        if (accountPresenter.gender.equals("")) {
            makeSnackBar(getString(R.string.gender_error));
            return;
        }

        accountPresenter.phoneNumber = localizationPresenter.country + phone;
        accountPresenter.ID = localizationPresenter.id;
        getLocation();
    }

    private void send() {

        accountViewModel.addPatient(accountPresenter.toJson()).observe(this, result -> {
            if (result == null) {
                makeSnackBar(getString(R.string.error_creation));
                return;
            }
            if (result.ID == null) {
                makeSnackBar(getString(R.string.error_creation));
            } else {
                Preference.setActive(getContext(), result.ID, accountPresenter.phoneNumber);
                makeSnackBar(getString(R.string.successful_creation));
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

    private void getLocation() {
        LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation().addOnCompleteListener(
                task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        accountPresenter.longitude = location.getLongitude();
                        accountPresenter.latitude = location.getLatitude();
                        send();
                    }
                }
        );
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(
                mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location mLastLocation = locationResult.getLastLocation();
                        accountPresenter.longitude = mLastLocation.getLongitude();
                        accountPresenter.latitude = mLastLocation.getLatitude();
                        send();
                    }
                },
                Looper.myLooper()
        );

    }
}
