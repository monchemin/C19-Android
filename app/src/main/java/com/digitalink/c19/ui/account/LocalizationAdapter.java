package com.digitalink.c19.ui.account;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.LocalizationPresenter;

import java.util.List;
import java.util.stream.Collectors;


public class LocalizationAdapter extends RecyclerView.Adapter<LocalizationAdapter.LocalizationViewHolder> {

    private List<LocalizationPresenter> dataSet, unMutableDataSet;
    private View.OnClickListener mOnItemClickListener;

    @NonNull
    @Override
    public LocalizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.view_localization_item, parent, false);
        return new LocalizationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalizationViewHolder holder, int position) {
        holder.displayItem(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void setData(List<LocalizationPresenter> presenters) {
        unMutableDataSet = presenters;
        dataSet = presenters;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filter(String text) {
        if (text == null || text.equals("")) {
            dataSet = null;
        } else {
            dataSet = unMutableDataSet
                    .stream()
                    .filter(c -> c.position.toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        notifyDataSetChanged();

    }

    public LocalizationPresenter getItem(int position) {
        return dataSet != null ? dataSet.get(position) : null;

    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    class LocalizationViewHolder extends RecyclerView.ViewHolder {

        TextView item;
        LocalizationPresenter presenter;

        LocalizationViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.localization_item);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        void displayItem(LocalizationPresenter presenter) {
            this.presenter = presenter;
            item.setText(presenter.position);
        }
    }
}

