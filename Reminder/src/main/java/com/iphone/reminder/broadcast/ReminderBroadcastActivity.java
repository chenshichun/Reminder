package com.iphone.reminder.broadcast;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iphone.reminder.R;

/**
 * Created by cfb on 17-3-15.
 */
public class ReminderBroadcastActivity extends Activity{
    private TextView titleTxt,contextTxt,timeTxt;
    private LinearLayout broadcastLiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.broadcast_reminder);

        titleTxt = (TextView) findViewById(R.id.broadcast_title);
        contextTxt = (TextView) findViewById(R.id.broadcast_context);
        timeTxt = (TextView) findViewById(R.id.broadcast_title_time);
        broadcastLiLayout = (LinearLayout) findViewById(R.id.broadcast_reminders);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("TITLE");
        String note = bundle.getString("NOTE");
        String currentTime = bundle.getString("CTIME");
        Log.d("cfb","title=="+title+"---note=="+note+"---currentTime==="+currentTime);
        titleTxt.setText(this.getResources().getString(R.string.reminder_pro));
        contextTxt.setText(title);
        timeTxt.setText(currentTime);

        broadcastLiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ReminderBroadcastActivity.this, ReminderBroadcast.class);
                intent.setAction("VIDEO_TIMER_STOP");
                sendBroadcast(intent);
            }
        });
    }
}
