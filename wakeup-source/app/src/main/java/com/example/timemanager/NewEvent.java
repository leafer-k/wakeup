package com.example.timemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    int id = 1;
    Calendar actualTime;
    TimePicker eventTime;
    Button addButton;
    CheckBox isDaily;
    EditText description_tv;
    Database mDBConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        actualTime = Calendar.getInstance();

        eventTime = findViewById(R.id.newEventTime);
        addButton = findViewById(R.id.addEventButton);
        isDaily = findViewById(R.id.isDaily);
        description_tv = findViewById(R.id.eventDescription);

        eventTime.setIs24HourView(true);
        eventTime.setCurrentHour(actualTime.get(Calendar.HOUR_OF_DAY));
        eventTime.setCurrentMinute(actualTime.get(Calendar.MINUTE));

        mDBConnector = new Database(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getInt("EventsID");
        }

        addButton.setOnClickListener(v -> {
            if (description_tv.getText().toString().equals("")) description_tv.setText("Будильник " + id);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDBConnector.insert(eventTime.getHour(), eventTime.getMinute(), description_tv.getText().toString(), true, isDaily.isChecked());
            }
            else mDBConnector.insert(eventTime.getCurrentHour(), eventTime.getCurrentMinute(), description_tv.getText().toString(), true, isDaily.isChecked());
            Toast.makeText(getApplicationContext(), "Будильник добавлен", Toast.LENGTH_LONG);
            finish();
        });
    }
}