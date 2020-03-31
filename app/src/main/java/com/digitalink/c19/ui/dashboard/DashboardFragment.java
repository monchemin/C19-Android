package com.digitalink.c19.ui.dashboard;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.ConnectionPresenter;
import com.digitalink.c19.presenter.CountryPresenter;
import com.digitalink.c19.presenter.HealthConstantPresenter;
import com.digitalink.c19.utils.Preference;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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
    private LineChart mChart;
    private List<HealthConstantPresenter> healthConstantPresenters;
    private List<String> xAxisLabel = new ArrayList<>();
    private MaterialButton button;
    private boolean isConnected;
    ScrollView rootLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rootLayout = root.findViewById(R.id.scrollView);
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

        mChart = root.findViewById(R.id.chart);
        mChart.setNoDataText(getString(R.string.chart_no_data));
        phoneNumber = root.findViewById(R.id.phone_number);
        dashboardInfo = root.findViewById(R.id.dashboard_info);
        button = root.findViewById(R.id.btn_login);
        button.setOnClickListener(v -> {
            if (!isConnected) {
                validateAndConnect();
            } else {
                init();
            }

        });
        return root;
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        button.setText(R.string.btn_login_text);
        dashboardInfo.setText(R.string.dashboard_init_text);
        dashboardInfo.setTextColor(R.color.colorPrimary);
        Preference.Disconnect(getContext());
        mChart.clear();
        mChart.setNoDataText(getString(R.string.chart_no_data));
        isConnected = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countryPresenters != null) {
            dashboardViewModel.getCountries().observe(this, result -> {
                if (result != null) {
                    countryPresenters = result.response;
                    fillCountrySpinner();
                }
            });
        }
        if (!Preference.GetPhoneNumber(getContext()).equals("")) {
            mChart.clear();
            isConnected = true;
            connectionPresenter.phoneNumber = Preference.GetPhoneNumber(getContext());
            connection();
        }

    }

    private void makeSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(rootLayout, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.close, v -> {
            snackbar.dismiss();
        });
        snackbar.show();
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

    private void validateAndConnect() {
        if (selectedCountry.equals("code")) {
            phoneNumber.setError(getString(R.string.missing_code));
            return;
        }
        String phone = phoneNumber.getText().toString();
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneNumber.setError(getString(R.string.missing_field));
            return;
        }

        connectionPresenter.phoneNumber = selectedCountry + phone;
        connection();
    }

    private void connection() {

        dashboardViewModel.connection(connectionPresenter.toJson()).observe(this, result -> {
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
            healthConstantPresenters = result.response.dailyInformation;
            button.setText(R.string.disconnection);
            if (!isConnected) {
                makeSnackBar(getString(R.string.success_connection));
                Preference.setActive(getContext(), result.response.ID, result.response.phoneNumber);
                isConnected = true;
            }
            if (result.response.dailyInformation != null) {
                setData();
                dashboardInfo.setText("");
            }
        });
    }

    public void renderData() {

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        xAxis.setTextSize(11);
        xAxis.setGranularityEnabled(true);

        LimitLine ll1 = new LimitLine(37.5f);
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setTextSize(10f);
        ll1.setLineColor(Color.GREEN);

        LimitLine ll2 = new LimitLine(40f);
        ll2.setLineWidth(2f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.RED);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(40f);
        leftAxis.setAxisMinimum(30f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        Legend legend = mChart.getLegend();
        legend.setYEntrySpace(60);
        legend.setFormToTextSpace(5);
        legend.setStackSpace(60);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        mChart.notifyDataSetChanged();
        mChart.invalidate();

    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();
        int index = 1;
        xAxisLabel.clear();

        for (HealthConstantPresenter hp : this.healthConstantPresenters) {
            values.add(new Entry(index, hp.temperature));
            xAxisLabel.add(hp.formatDate(getString(R.string.date_format)));
            if (index == 5) {
                break;
            }
            index++;
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, getString(R.string.chart_legend));
        set1.setDrawIcons(false);
        set1.setColor(ContextCompat.getColor(getContext(), R.color.color1));
        set1.setCircleColor(R.color.color1);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFormLineWidth(1f);
        set1.setFormSize(15.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        renderData();
    }


}
