package com.example.timemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditFragment extends DialogFragment {
    TimePicker tp;
    TextView description_tv;
    Switch isActive;
    CheckBox isCycle;
    Database mDBConnector;
    ImageView delete;
    int id;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        id = getArguments().getInt("id");
        mDBConnector = new Database(getActivity());
        return builder.setView(R.layout.edit_window)
                .setPositiveButton("ОК", (dialog, which) -> {
                    Event event = new Event(id, tp.getCurrentHour(), tp.getCurrentMinute(),
                            description_tv.getText().toString(), isActive.isChecked(), isCycle.isChecked());
                    mDBConnector.update(event);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).updateList();
                    }
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                }).create();
    }

    @Override
    public void onResume() {
        super.onResume();
        delete = getDialog().getWindow().findViewById(R.id.deleteButton);
        delete.setOnClickListener(v -> {
            mDBConnector.delete(id);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateList();
            }
            getDialog().dismiss();
        });
        tp = getDialog().getWindow().findViewById(R.id.timePickerEdit);
        description_tv = getDialog().getWindow().findViewById(R.id.descriptionEdit);
        isActive = getDialog().getWindow().findViewById(R.id.isActiveEdit);
        isCycle = getDialog().getWindow().findViewById(R.id.isDailyEdit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setHour(getArguments().getInt("hour"));
            tp.setMinute(getArguments().getInt("minute"));
        } else {
            tp.setCurrentHour(getArguments().getInt("hour"));
            tp.setCurrentMinute(getArguments().getInt("minute"));
        }

        description_tv.setText(getArguments().getString("description").replace("_", " "));
        isActive.setChecked(getArguments().getBoolean("isActive"));
        isCycle.setChecked(getArguments().getBoolean("isCycle"));
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.round_item));
        tp.setIs24HourView(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
