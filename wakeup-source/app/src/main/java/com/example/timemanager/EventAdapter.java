package com.example.timemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final ArrayList<Event> eventList;
    private final EventAdapter.OnItemClick itemClick;

    public EventAdapter(ArrayList<Event> eventList, EventAdapter.OnItemClick itemClick) {
        this.eventList = eventList;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        holder.bind(eventList.get(position), itemClick);
    }

    @Override
    public int getItemCount() {
        try {
            return eventList.size();
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public void setData(List<Event> data) {
        eventList.clear();
        eventList.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView description;
        private final TextView time;
        private final SwitchCompat isActive;
        private final ConstraintLayout mainLayout;
        Database mDBConnector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.alarmDescription);
            time = itemView.findViewById(R.id.alarmTime);
            isActive = itemView.findViewById(R.id.isActive);
            mainLayout = itemView.findViewById(R.id.mainContainer);
        }

        public void bind(final Event event, final EventAdapter.OnItemClick itemClick) {
            String minute = event.getMinute() < 10 ? String.format(Locale.getDefault(),"0%d", event.getMinute()) : Integer.toString(event.getMinute());
            time.setText(event.getHour() + ":" + minute);
            description.setText(event.getDescription().replace("_", " "));
            isActive.setChecked(event.getIsActive());
            mDBConnector = new Database(isActive.getContext());
            isActive.setOnClickListener(v -> {
                event.setActive(isActive.isChecked());
                mDBConnector.update(event);
            });
            itemView.setOnClickListener(v -> itemClick.onClick(itemView, event));
        }
    }

    public interface OnItemClick {
        void onClick(View v, Event event);
    }
}
