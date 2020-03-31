package com.digitalink.c19.ui.history;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.ConnectionPresenter;
import com.digitalink.c19.presenter.HealthConstantPresenter;
import com.digitalink.c19.utils.Preference;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private List<HealthConstantPresenter> presenters;
    private ScrollView rootLayout;
    private ConnectionPresenter connectionPresenter = new ConnectionPresenter();

    private HistoryAdapter adapter;
    private boolean isConnected;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        rootLayout = root.findViewById(R.id.scrollView);

        RecyclerView recyclerView = root.findViewById(R.id.history_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new HistoryAdapter(getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Preference.GetPhoneNumber(getContext()).equals("")) {
            isConnected = true;
            connectionPresenter.phoneNumber = Preference.GetPhoneNumber(getContext());
            connection();
        }

    }

    private void connection() {

        historyViewModel.connection(connectionPresenter.toJson()).observe(this, result -> {
            if (result == null) {
                makeSnackBar(getString(R.string.connection_error));
                return;
            }
            if (result.response == null) {
                makeSnackBar(getString(R.string.connection_error));
                return;
            }
            if (result.response.ID.equals("")) {
                makeSnackBar(getString(R.string.connection_error));
                return;
            }
            presenters = result.response.dailyInformation;

            if (!isConnected) {
                makeSnackBar(getString(R.string.success_connection));
                Preference.setActive(getContext(), result.response.ID, result.response.phoneNumber);
                isConnected = true;
            }
            if (result.response.dailyInformation != null) {
               adapter.setData(presenters, getString(R.string.date_format2));
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
