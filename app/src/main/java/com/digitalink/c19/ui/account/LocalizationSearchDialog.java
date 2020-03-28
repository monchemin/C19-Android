package com.digitalink.c19.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalink.c19.R;
import com.digitalink.c19.presenter.LocalizationPresenter;
import com.digitalink.c19.ui.ActionChooseListener;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class LocalizationSearchDialog extends DialogFragment {

    private List<LocalizationPresenter> presenters;
    private LocalizationAdapter adapter = new LocalizationAdapter();

    private View.OnClickListener onItemClickListener = view -> {

        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
        loadItem(viewHolder.getAdapterPosition());
    };
    private ActionChooseListener onInputListener;

    public LocalizationSearchDialog(List<LocalizationPresenter> presenters) {
        this.presenters = presenters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_localization_select_dialog, container, false);
        (rootView.findViewById(R.id.button_close)).setOnClickListener(v -> loadItem(0));
        SearchView searchView = rootView.findViewById(R.id.localization_search_view);
        searchView.onActionViewExpanded();
        RecyclerView recyclerView = rootView.findViewById(R.id.localization_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //  try {
        //      onInputListener = (ActionChooseListener) getActivity();
        //   } catch (ClassCastException e) {
        //      Log.e(TAG, "onAttach: " + e.getMessage());
        // }
    }

    public void setListener(ActionChooseListener action) {
        onInputListener = action;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setData(presenters);
        adapter.setOnItemClickListener(onItemClickListener);
    }

    private void loadItem(int position) {
        System.out.println("nyemo " + position);
        LocalizationPresenter item = adapter.getItem(position);
        if (item != null) {
            System.out.println("nyemo " + item.id);
            onInputListener.sendInput(item.id);
        } else {
            onInputListener.sendInput("");
        }
        dismiss();
    }

}
