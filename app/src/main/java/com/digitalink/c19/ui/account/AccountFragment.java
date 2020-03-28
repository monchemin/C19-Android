package com.digitalink.c19.ui.account;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.List;

public class AccountFragment extends Fragment implements ActionChooseListener {

    private AccountViewModel accountViewModel;
    private List<LocalizationPresenter> presenters;
    private TextInputEditText localization, phoneNumber, age, weight;
    private AccountPresenter accountPresenter = new AccountPresenter();
    private MaterialButton button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        localization = root.findViewById(R.id.localization);
        phoneNumber = root.findViewById(R.id.phone_number);
        age = root.findViewById(R.id.age);
        weight = root.findViewById(R.id.weight);
        button = root.findViewById(R.id.btn_create_account);
        localization.setOnClickListener(v -> {
            showDialog();
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
        System.out.println("nyemo " + input);
        for (LocalizationPresenter presenter : presenters) {
            if (presenter.id.equals(input)) {
                localization.setText(presenter.position);
                accountPresenter.ID = input;
                break;
            }
        }

    }

    private void validate() {
        accountPresenter.phoneNumber = phoneNumber.getText().toString();
        if (accountPresenter.phoneNumber.isEmpty() || !Patterns.PHONE.matcher(accountPresenter.phoneNumber).matches()) {
            phoneNumber.setError("phone");
            return;
        }
        if (accountPresenter.ID == null || accountPresenter.ID.length() == 0) {
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
        System.out.println("nyemo " + accountPresenter.toJson());
    }
}
