package com.example.timemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AlarmActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setClassName("com.example.timemanager", "com.example.timemanager.AlarmActivity");
        intent1.putExtra("id", (int)intent.getExtras().get("id"));
        context.startActivity(intent1);
     //   Toast.makeText(context, "Будильник сработал!", Toast.LENGTH_LONG).show();
    }
}
