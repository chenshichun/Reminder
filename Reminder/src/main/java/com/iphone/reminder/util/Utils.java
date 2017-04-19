package com.iphone.reminder.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.iphone.reminder.activity.MainActivity;
import com.iphone.reminder.broadcast.ReminderBroadcast;
import com.iphone.reminder.data.MessageBean;
import com.iphone.reminder.sqlite.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final int TO_Y_VALUE = 1050;
    public static final int MARGIN_GAP = 150;
    public static final int BOTTOM_GAP=5;
    public static final int SEY_Y = TO_Y_VALUE + MARGIN_GAP;
    public static boolean isShow = false;
    /**
     * 判断软键盘是否弹出
     */
    public static boolean isSoftShowing(Activity mActivity) {
        // 获取当前屏幕内容的高度
        int screenHeight = mActivity.getWindow().getDecorView().getHeight();
        // 获取View可见区域的bottom
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    /**
     * 把数据库数据转到可以让adapter使用的ArrayList数据
     */
    public static ArrayList<MessageBean> addTableOneDetailsDate(Context mContext) {
        SQLHelper.createSql(mContext);

        List<Map<String, String>> listTemp = SQLHelper.queryAllData(
                mContext);// 调用数据库数据
        ArrayList<MessageBean> mMessageList = new ArrayList<MessageBean>();
        if (listTemp.size() > 0) {
            for (int i = 0; i < listTemp.size(); i++) {
                MessageBean item = new MessageBean();
                item.title = listTemp.get(i).get("TITLE");
                item.time = listTemp.get(i).get("TIME");
                item.location = listTemp.get(i).get("LOCATION");
                item.note = listTemp.get(i).get("NOTE");
                item.repeat = Integer.valueOf(listTemp.get(i).get("REPEAT"));
                item.isCheck = listTemp.get(i).get("ISCHECK").equals("0")?false:true;
                mMessageList.add(item);
            }
        }

        return mMessageList;
    }

    /**
     * 把数据库数据转到可以让adapter使用的ArrayList数据
     */
    public static ArrayList<MessageBean> addDetailsDate(Context mContext,
                                                        int tabelCount) {
        SQLHelper.createSql(mContext);

        List<Map<String, String>> listTemp = SQLHelper.queryAllMessage(
                mContext, tabelCount);// 调用数据库数据
        ArrayList<MessageBean> mMessageList = new ArrayList<MessageBean>();
        if (listTemp.size() > 0) {
            for (int i = 0; i < listTemp.size(); i++) {
                MessageBean item = new MessageBean();
                item.title = listTemp.get(i).get("TITLE");
                item.time = listTemp.get(i).get("TIME");
                item.location = listTemp.get(i).get("LOCATION");
                item.note = listTemp.get(i).get("NOTE");
                item.repeat = Integer.valueOf(listTemp.get(i).get("REPEAT"));
                item.isCheck = listTemp.get(i).get("ISCHECK").equals("0")?false:true;
                if(!Utils.isShow&&item.isCheck) {
                }else{
                    mMessageList.add(item);
                }
            }
        }

        return mMessageList;
    }


    /**
     * 把数据库数据转到可以让adapter使用的ArrayList数据
     */
    public static ArrayList<MessageBean> addSearchData(Context mContext, String keywords) {
        SQLHelper.createSql(mContext);

        List<Map<String, String>> listTemp = SQLHelper.queryAllTitle(
                mContext, keywords);// 调用数据库数据
        ArrayList<MessageBean> mMessageList = new ArrayList<MessageBean>();
        if (listTemp.size() > 0) {
            for (int i = 0; i < listTemp.size(); i++) {
                MessageBean item = new MessageBean();
                item.title = listTemp.get(i).get("TITLE");
                item.time = listTemp.get(i).get("TIME");
                item.location = listTemp.get(i).get("LOCATION");
                item.note = listTemp.get(i).get("NOTE");
                item.repeat = Integer.valueOf(listTemp.get(i).get("REPEAT"));
                item.isCheck = listTemp.get(i).get("ISCHECK").equals("0")?false:true;
                mMessageList.add(item);
            }
        }

        return mMessageList;
    }


    /**
     * 一次性闹钟
     */
    public static void singleReminder(Activity activity, Context context,
                                      String title, String note, long timeStamp, String currentTime) {
        Intent intent = new Intent(activity, ReminderBroadcast.class);
        intent.setAction("VIDEO_TIMER");
        intent.putExtra("TITLE", title);
        intent.putExtra("NOTE", note);
        intent.putExtra("CTIME", currentTime);
        PendingIntent sender = PendingIntent.getBroadcast(activity, 0, intent,
                0);

        AlarmManager am = (AlarmManager) context
                .getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, timeStamp,
                sender);
    }

    /**
     * 重复闹钟
     */
    public static void alarmRepeatReminder(Activity activity, Context context,
                                           String title, String note, long timeStamp, long intervalMillis, String currentTime) {
        Intent intent = new Intent(activity, ReminderBroadcast.class);
        intent.setAction("VIDEO_TIMER");
        intent.putExtra("TITLE", title);
        intent.putExtra("NOTE", note);
        intent.putExtra("CTIME", currentTime);
        Log.d("cfb","utils===="+title+"---note=="+note+"````currentTime---"+currentTime);
        // PendingIntent这个类用于处理即将发生的事情
        PendingIntent sender = PendingIntent.getBroadcast(activity, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        @SuppressWarnings("static-access")
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                timeStamp, intervalMillis, sender);
    }

    /**
     * 将字符串转为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(user_time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long l = d.getTime();
        String str = String.valueOf(l);
        re_time = str.substring(0, 10);

        return re_time;
    }

    /**
     * 判断搜索条件
     */
    public static boolean iskeywordsSearched(String titleStr, String keywords) {
        if (titleStr.indexOf(keywords) != -1) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValidTime(long date1, long date2) {
        //SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean isValidTime = false;
        //try {

            //double beginTime = currentTime.parse(date1);
            //double endTime = currentTime.parse(date2);

            if(date2-date1>0){
                isValidTime =  true;
            }else{
                isValidTime =  false;
            }
           Log.d("cfb","isValidTime==="+isValidTime);
        /*} catch (java.text.ParseException e) {
            e.printStackTrace();
        }*/
        return  isValidTime;
    }

    //判断当前软键盘状态
    public static boolean isHideOrShowImm(View v){
        InputMethodManager imm = (InputMethodManager) MainActivity.mainActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm.hideSoftInputFromWindow(v.getWindowToken(), 0))
        {
            //软键盘已弹出
            return true;
        }
        else
        {
            //软键盘未弹出
            return false;
        }

    }

    // 隐藏输入法
    public static void hideImm(){

            InputMethodManager imm = (InputMethodManager) MainActivity.mainActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
