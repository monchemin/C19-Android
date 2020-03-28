package com.digitalink.c19.ui.account;

import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.presenter.AccountPresenter;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.digitalink.c19.ui.ActionChooseListener;
import com.digitalink.c19.utils.Preference;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AccountFragment extends Fragment implements ActionChooseListener {

    private AccountViewModel accountViewModel;
    private List<LocalizationPresenter> presenters;
    private TextInputEditText localization, phoneNumber, age, weight;
    private AccountPresenter accountPresenter = new AccountPresenter();
    private LocalizationPresenter localizationPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        localization = root.findViewById(R.id.localization);
        phoneNumber = root.findViewById(R.id.phone_number);
        age = root.findViewById(R.id.age);
        weight = root.findViewById(R.id.weight);

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

        MaterialButton button = root.findViewById(R.id.btn_create_account);
        localization.setOnClickListener(v -> {
            if (presenters != null) {
                showDialog();
            }

        });
        button.setOnClickListener(v -> validate());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        accountViewModel.locations().observe(getViewLifecycleOwner(), results -> {
            if (results.response != null) {
                presenters = results.response;
            }
        });
    }

    private void showDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        LocalizationSearchDialog newFragment = new LocalizationSearchDialog(presenters);
        newFragment.setListener(this);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    @Override
    public void sendInput(String input) {
        for (LocalizationPresenter presenter : presenters) {
            if (presenter.id.equals(input)) {
                localization.setText(presenter.position);
                localizationPresenter = presenter;
                break;
            }
        }

    }

    private void validate() {
        String phone = phoneNumber.getText().toString();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneNumber.setError("phone");
            return;
        }
        if (localizationPresenter == null) {
            localization.setError("location");
            return;
        }
        try {
            accountPresenter.age = Integer.parseInt(age.getText().toString());
        } catch (NumberFormatException e) {
            age.setError("age");
            return;
        }
        try {
            accountPresenter.weight = Double.parseDouble(weight.getText().toString());
        } catch (NumberFormatException e) {
            weight.setError("weight");
            return;
        }
        accountPresenter.phoneNumber = localizationPresenter.country + phone;
        accountPresenter.ID = localizationPresenter.id;
        getLocation();
    }

    private void send() {
        System.out.println("nyemo " + accountPresenter.toJson());

        accountViewModel.addPatient(accountPresenter.toJson()).observe(this, result -> {
            if (result.error == null && result.ID != null) {
                System.out.println("nyemo " + result.ID);
                Preference.setActive(getContext(), result.ID, accountPresenter.phoneNumber);
            } else {
                Toast.makeText(getContext(), "error occur", Toast.LENGTH_LONG).show();
            }
        });


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
