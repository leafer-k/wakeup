package com.example.timemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {
    Database mDBConnector;
    TextView currTimeTV;
    TextView questionTV;
    TextView descTV;
    EditText answer;
    Calendar calendar;
    Vibrator vibrator;
    Button answerButton;
    ImageView backgroundImg;
    MediaPlayer mp;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        mp = MediaPlayer.create(this, R.raw.alarm_clock);
        mp.start();
        mp.setLooping(true);

        calendar = Calendar.getInstance();
        mDBConnector = new Database(this);
        answerButton = findViewById(R.id.answerButton);
        answer = findViewById(R.id.answerTextEdit);
        backgroundImg = findViewById(R.id.backgroundImg);
        questionTV = findViewById(R.id.questionTextView);
        descTV = findViewById(R.id.descriptionWhileAlarm);

        backgroundImg.setAlpha(0.4f);
        backgroundImg.setTranslationY(100);
        backgroundImg.animate().alpha(0.5f).translationY(0).setDuration(500);

        questionTV.setAlpha(0f);
        questionTV.setTranslationY(100);
        questionTV.animate().alpha(1f).translationY(0).setDuration(500);

        Intent intent = getIntent();

        int id = (int) intent.getExtras().get("id");
        long[] pattern = {0, 1000, 1000};


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.cancel(pendingIntent);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        Event event = mDBConnector.select(id);

        if (!event.isCycle()) {
            event.setActive(false);
            mDBConnector.update(event);
        }

        descTV.setText(event.getDescription());

        String minute = event.getMinute() < 10 ? String.format(Locale.getDefault(),
                "0%d", event.getMinute()) : Integer.toString(event.getMinute());

        currTimeTV = findViewById(R.id.currentTime);
        currTimeTV.setText(event.getHour() + ":" + minute);

        makeQuestion();

        answerButton.setOnClickListener(v -> {
            if (Integer.toString(key).equals(answer.getText().toString())) {
                vibrator.cancel();
                mp.stop();
                finish();
            } else {
                makeQuestion();
                questionTV.setAlpha(0f);
                questionTV.setTranslationY(100);
                questionTV.animate().alpha(1f).translationY(0).setDuration(500);
                Toast.makeText(this, "Неверно!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeQuestion() {
        int a, b, c;
        a = new Random().nextInt(100 + 100) - 100;
        b = new Random().nextInt(100 + 100) - 100;
        c = new Random().nextInt(100 + 100) - 100;

        String sb = b > 0 ? "+ " + b : "- " + -b;
        String sc = c > 0 ? "+ " + c : "- " + -c;

        questionTV.setText(a + " " + sb + " " + sc + " = ?");
        key = a + b + c;
    }


}