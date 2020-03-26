package com.digitalink.c19.ui.HealthConstant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;

public class HealthConstantFragment extends Fragment {

    private HealthConstantViewModel healthConstantViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthConstantViewModel =
                ViewModelProviders.of(this).get(HealthConstantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health_constant, container, false);

        healthConstantViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });
        return root;
    }
}
