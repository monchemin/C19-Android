package com.digitalink.c19.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.base.BasePresenter;
import com.digitalink.c19.presenter.LocationsPresenter;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        accountViewModel.locations().observe(getViewLifecycleOwner(), new Observer<BasePresenter<LocationsPresenter>>() {
            @Override
            public void onChanged(BasePresenter<LocationsPresenter> results) {
                System.out.println("nyemo " + results.response.get(0).country);
            }
        });
        return root;
    }
}
