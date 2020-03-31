package com.digitalink.c19.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.HealthConstantPresenter;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HealthConstantPresenter> dataSet;
    private View.OnClickListener mOnItemClickListener;
    private String dateFormatter;
    FragmentManager fragmentManager;

     HistoryAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.view_history_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.displayItem(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void setData(List<HealthConstantPresenter> presenters, String dateFormatter) {
        dataSet = presenters;
        this.dateFormatter = dateFormatter;
        notifyDataSetChanged();
    }

    public HealthConstantPresenter getItem(int position) {
        return dataSet != null && dataSet.size() != 0 ? dataSet.get(position) : null;

    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView dateTime, temperature;
        HealthConstantPresenter presenter;
        LinearLayout root;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.root_item);
            dateTime = itemView.findViewById(R.id.date_time);
            temperature = itemView.findViewById(R.id.temperature);
            itemView.setTag(this);
            itemView.setOnClickListener(v -> { showDialog();});
        }

        void displayItem(HealthConstantPresenter presenter) {
            this.presenter = presenter;
            temperature.setText(""+presenter.temperature);
            dateTime.setText(presenter.formatDate(dateFormatter));
        }

        private void showDialog() {
            HistoryDialog newFragment = new HistoryDialog(presenter);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        }
    }
}

