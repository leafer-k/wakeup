package com.example.timemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EventAdapter adapter;
    Database mDBConnector;
    ArrayList<Event> allEvents;
    AlarmManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        mDBConnector = new Database(this);
        try {
            allEvents = mDBConnector.selectAll();
        } catch (Exception exception) {
            Log.e("DB error", exception.getMessage());
        }

        recyclerView = findViewById(R.id.alarmRecycler);
        adapter = new EventAdapter(allEvents, (v, event) -> {
            EditFragment fragment = new EditFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", event.getId());
            bundle.putInt("hour", event.getHour());
            bundle.putInt("minute", event.getMinute());
            bundle.putString("description", event.getDescription());
            bundle.putBoolean("isActive", event.getIsActive());
            bundle.putBoolean("isCycle", event.isCycle());
            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), "Dialog");
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setAlpha(0f);
        recyclerView.setTranslationY(100);
        recyclerView.animate().alpha(1f).translationY(0).setDuration(500);
        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
        recyclerView.setAlpha(0f);
        recyclerView.setTranslationY(100);
        recyclerView.animate().alpha(1f).translationY(0).setDuration(500);
    }

    public void updateList() {
        try {
            allEvents = mDBConnector.selectAll();
        } catch (Exception exception) {
            Log.e("DB error", exception.getMessage());
        }
        adapter.setData(allEvents);
        updateAlarms();
    }

    public final void setAlarm(Event event) {
        Intent intent = new Intent(this, AlarmReciever.class);
        intent.putExtra("id", event.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, event.getId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, event.getHour());
        c.set(Calendar.MINUTE, event.getMinute());
        c.set(Calendar.SECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void updateAlarms() {
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getIsActive()) {
                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < allEvents.get(i).getHour() ||
                        (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == allEvents.get(i).getHour() &&
                                allEvents.get(i).getMinute() > Calendar.getInstance().get(Calendar.MINUTE))) {
                    setAlarm(allEvents.get(i));
                }
            }
        }
    }

    public void newEvent(View view) {
        Intent intent = new Intent(this, NewEvent.class);
        try {
            intent.putExtra("EventsID", allEvents.size() + 1);
        } catch (NullPointerException exception) {
            intent.putExtra("EventsID", 1);
        }
        startActivity(intent);
    }
}