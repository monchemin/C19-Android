package com.digitalink.c19.ui.account;

import android.os.Bundle;
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
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.digitalink.c19.ui.ActionChooseListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AccountFragment extends Fragment implements ActionChooseListener {

    private AccountViewModel accountViewModel;
    private List<LocalizationPresenter> presenters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        accountViewModel.locations().observe(getViewLifecycleOwner(), results -> {
            if (results.response != null) {
                presenters = results.response;
            }
        });
        TextInputEditText localization = root.findViewById(R.id.localization);
        localization.setOnClickListener(v -> {
         showDialog();
        });
        return root;
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

    }
}
