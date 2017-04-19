package com.iphone.reminder.broadcast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.iphone.reminder.R;
import com.iphone.reminder.activity.MainActivity;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final int TYPE_Hangup = 6;
    private NotificationManager manger;

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
        Log.d("cfb","---action==="+action);
		if (action != null && action.equals("VIDEO_TIMER")) {
            String title = bundle.getString("TITLE");
            String note = bundle.getString("NOTE");
            String currentTime = bundle.getString("CTIME");
            Log.d("cfb--ReminderBroadcast", "title:::::::::::" + title+"--TITLE=="+note+"---currentTime=="+currentTime+"---action==="+action);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("横幅通知");
            builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
            builder.setDefaults(NotificationCompat.PRIORITY_DEFAULT);
            builder.setSmallIcon(context.getApplicationInfo().icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
            Intent intento = new Intent(context,ReminderBroadcastActivity.class);
            intento.setAction("VIDEO_TIMER");
            intento.putExtra("TITLE", title);
            intento.putExtra("NOTE", note);
            intento.putExtra("CTIME", currentTime);
            PendingIntent pIntent = PendingIntent.getActivity(context,1,intento,0);
            builder.setContentIntent(pIntent);
            //这句是重点
            RemoteViews contentView = new RemoteViews(context.getPackageName(),
                    R.layout.broadcast_reminder);
            builder.setFullScreenIntent(pIntent,true);
            builder.setAutoCancel(true);
            Notification notification = builder.build();
            notification.contentView = contentView;
            manger = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manger.notify(TYPE_Hangup,notification);

            Uri rmUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context,rmUrl);
            if(r!= null) {
                r.play();
            }


			/*NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// 创建一个Notification
			Notification notification = new Notification();
			// 设置显示在手机最上边的状态栏的图标
			notification.icon = R.drawable.ic_launcher;
			// 当当前的notification被放到状态栏上的时候，提示内容
			notification.tickerText = "";*/

			/***
			 * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，
			 * 该Intent会被触发 notification.contentView:我们可以不在状态栏放图标而是放一个view
			 * notification.deleteIntent 当当前notification被移除时执行的intent
			 * notification.vibrate 当手机震动时，震动周期设置
			 *//*
			// 添加声音提示
			notification.defaults = Notification.DEFAULT_SOUND;
			// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
			notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

			Intent intent1 = new Intent(context, ReminderBroadcastActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent1, PendingIntent.FLAG_ONE_SHOT);
			// 点击状态栏的图标出现的提示信息设置
			notification
					.setLatestEventInfo(context, title, note, pendingIntent);
			manager.notify((int) System.currentTimeMillis(), notification);*/

            /*Uri rmUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone r = RingtoneManager.getRingtone(context,rmUrl);
            r.play();*/
		}else if(action != null && action.equals("VIDEO_TIMER")){
            manger.cancel(TYPE_Hangup);
        }
	}
}
