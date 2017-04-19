package com.iphone.reminder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iphone.reminder.R;
import com.iphone.reminder.sqlite.SQLHelper;
import com.iphone.reminder.util.Utils;
import com.iphone.reminder.view.SwitchButton;
import com.iphone.reminder.wheelview.OnWheelChangedListener;
import com.iphone.reminder.wheelview.OnWheelScrollListener;
import com.iphone.reminder.wheelview.WheelView;
import com.iphone.reminder.wheelview.adapters.AbstractWheelTextAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DetailsActivity extends Activity implements OnClickListener {

	private Button doneBtn;
	private EditText reminderTitleEt, reminderNoteEt;
	private View lyDialogPickerLineView;
	private RelativeLayout reminderTimeRl,repeatRl,reminderLocationRl;
	private TextView reminderTimePromptTv, reminderTimeContentTv,
			reminderTimeTv,reminderLocationTxt,reminderListTxt,reminderRepeatDetailTxt;
	private boolean isReminderTimeShow = false;
    private SwitchButton remindOnDataCb,remindOnLocationCb;
    private LinearLayout remindOnDataLl;
	
	private String reminderTime = null;
	private String reminderContextTime = null;

	private int MIN_YEAR = 2000;
	private int MAX_YEAR = 2020;

	public static final int DIALOG_MODE_CENTER = 0;
	public static final int DIALOG_MODE_BOTTOM = 1;

	private Context context;
	private WheelView wvDate;
	private WheelView wvHour;
	private WheelView wvMinute;

	private ViewGroup VDialogPicker;

	private ArrayList<String> arry_dates = new ArrayList<String>();
	private ArrayList<String> arry_hours = new ArrayList<String>();
	private ArrayList<String> arry_minutes = new ArrayList<String>();
	private CalendarTextAdapter mDateAdapter;
	private CalendarTextAdapter mHourAdapter;
	private CalendarTextAdapter mMinuteAdapter;

	private int hour;
	private int minute;

	private int currentDate = 0;
	private int currentHour = 0;
	private int currentMinute = 0;

	private int maxTextSize = 19;
	private int minTextSize = 16;

	private boolean issetdata = false;

	private int selectDate;
	private String selectHour;
	private String selectMinute;
	private int currentTableCount;
	private String currentPosition, detailTitle;
	private String timeStamp;

    SharedPreferences repeatSp;
    SharedPreferences.Editor editor;
    private int repeatNum;
	private String date;
    private String nowTime;
    private String endTime;
    private final static int REQUESTCODE = 1;
    private int repeatTime = 0;

    private Calendar calendarNowTime, calendarEndTime;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.details_activity);
		//this.getActionBar().hide();
		//setWindowStatusBarColor(this, 0xffffff);
		Intent intent = getIntent();
		currentTableCount = intent.getIntExtra("TABLE_COUNT", Utils.TABLE_COUNT_ONE); // 确定操作的是哪张表
		currentPosition = intent.getStringExtra("POSITION"); // 操作的是哪一行
		detailTitle = intent.getStringExtra("DETAIL_TITLE");
        if(intent.getStringExtra("DETAIL_REPEAT")==null){
            repeatNum = 1;
        }else{
            repeatNum = Integer.valueOf(intent.getStringExtra("DETAIL_REPEAT"));
        }
		Log.d("chenshichun", "currentPosition:: " + currentPosition);
		init();

		reminderTitleEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (reminderTitleEt.length() == 0) {
					doneBtn.setTextColor(0xAA999999);
				} else {
					doneBtn.setTextColor(0xAA007afe);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void init() {

		doneBtn = (Button) findViewById(R.id.done_btn);
		doneBtn.setOnClickListener(this);
		reminderTitleEt = (EditText) findViewById(R.id.reminder_title_et);
		lyDialogPickerLineView = (View) findViewById(R.id.ly_dialog_picker_line);
		reminderTimeRl = (RelativeLayout) findViewById(R.id.reminder_time_rl);
        reminderLocationRl = (RelativeLayout) findViewById(R.id.remind_location_rl);
		repeatRl =(RelativeLayout) findViewById(R.id.repeat_rl);
		reminderTimeRl.setOnClickListener(this);
		reminderTimePromptTv = (TextView) findViewById(R.id.reminder_time_prompt_tv);
		reminderTimeContentTv = (TextView) findViewById(R.id.reminder_time_content_tv);
		reminderTimeTv = (TextView) findViewById(R.id.reminder_time_tv);
		remindOnDataCb = (SwitchButton) findViewById(R.id.remind_on_data_sb);
		remindOnDataCb.setChecked(true);
        remindOnLocationCb = (SwitchButton) findViewById(R.id.remind_on_location_sb);
		remindOnLocationCb.setChecked(true);
		remindOnDataLl = (LinearLayout) findViewById(R.id.remind_on_data_ll);
		reminderNoteEt = (EditText) findViewById(R.id.reminder_note);
		reminderTitleEt.setText(detailTitle);
        reminderLocationTxt = (TextView) findViewById(R.id.location_remind_txt);
        reminderRepeatDetailTxt = (TextView) findViewById(R.id.repeat_details);
        reminderListTxt = (TextView) findViewById(R.id.list_name_txt);
        showReminderListName();

        remindOnDataCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOpen) {
                remindOnDataLl.setVisibility(isOpen ? View.VISIBLE
                        : View.GONE);
                isReminderTimeShow = true;
                isShowReminderWheelView(isReminderTimeShow);
            }
        });

        remindOnLocationCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOpen) {
                reminderLocationRl.setVisibility(isOpen ? View.VISIBLE : View.GONE);

            }
        });

        Log.d("cfb","-DetailsActivity==="+currentTableCount+"----...currentPosition==="+currentPosition+",,,,repeatNum==="+repeatNum);
		repeatRl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(DetailsActivity.this, AlarmRepeatActivity.class);
                intent.putExtra("TABLE_COUNT",currentTableCount);
                intent.putExtra("POSITION",Integer.valueOf(currentPosition));
                intent.putExtra("repeatNum",repeatNum);
				startActivityForResult(intent, REQUESTCODE);
			}
		});
		
        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年M月d日 E hh:mm");
        SimpleDateFormat sDateFormatContent = new SimpleDateFormat(
                "yy/M/dE hh:mm");
        endTime = currentTime.format(new java.util.Date());
        nowTime = currentTime.format(new java.util.Date());
        calendarNowTime =Calendar.getInstance(Locale.getDefault());
        calendarNowTime.setTimeInMillis(System.currentTimeMillis());
        calendarEndTime =Calendar.getInstance(Locale.getDefault());
        calendarEndTime.setTimeInMillis(System.currentTimeMillis());
        Log.d("cfb","calendarNowTime==="+calendarNowTime.get(Calendar.HOUR)+":::"+currentMinute+"========="+calendarNowTime.get(Calendar.MINUTE));
        calendarNowTime.set(Calendar.HOUR_OF_DAY, calendarNowTime.get(Calendar.HOUR_OF_DAY));
        calendarNowTime.set(Calendar.MINUTE, calendarNowTime.get(Calendar.MINUTE));
        calendarNowTime.set(Calendar.SECOND, 0);
        calendarNowTime.set(Calendar.MILLISECOND, 0);

        if(TextUtils.isEmpty(selectHour) || TextUtils.isEmpty(selectMinute)){
            selectHour = String.valueOf(calendarNowTime.get(Calendar.HOUR_OF_DAY));
            selectMinute = String.valueOf(calendarNowTime.get(Calendar.MINUTE));
            Log.d("cfb", "calendarEndTime==" + selectHour + ":::" + selectMinute + "==========" + calendarEndTime + "" + calendarEndTime);
            calendarEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(selectHour));
            calendarEndTime.set(Calendar.MINUTE, Integer.parseInt(selectMinute));
            calendarEndTime.set(Calendar.SECOND, 0);
            calendarEndTime.set(Calendar.MILLISECOND, 0);
        }

        date = sDateFormat.format(new java.util.Date());
        String dateContent = sDateFormatContent.format(new java.util.Date());
        reminderTimeTv.setText(date);
		reminderTimeContentTv.setText(dateContent);

		VDialogPicker = (ViewGroup) findViewById(R.id.ly_dialog_picker);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
		// 此处相当于布局文件中的Android:layout_gravity属性
		lp.gravity = Gravity.CENTER_VERTICAL;

		wvDate = new WheelView(getApplicationContext());
		wvDate.setLayoutParams(lp);
		VDialogPicker.addView(wvDate);

		wvHour = new WheelView(getApplicationContext());
		wvHour.setLayoutParams(lp);
		wvHour.setCyclic(true);
		VDialogPicker.addView(wvHour);

		wvMinute = new WheelView(getApplicationContext());
		wvMinute.setLayoutParams(lp);
		wvMinute.setCyclic(true);
		VDialogPicker.addView(wvMinute);

		if (!issetdata) {
			initTime();
		}
		initDates();
		mDateAdapter = new CalendarTextAdapter(getApplicationContext(),
				arry_dates, currentDate, maxTextSize, minTextSize);
		wvDate.setVisibleItems(5);
		wvDate.setViewAdapter(mDateAdapter);
		wvDate.setCurrentItem(currentDate);

		initHours(hour);
		mHourAdapter = new CalendarTextAdapter(getApplicationContext(),
				arry_hours, currentHour, maxTextSize, minTextSize);
		wvHour.setVisibleItems(5);
		wvHour.setViewAdapter(mHourAdapter);
		wvHour.setCurrentItem(currentHour);

		initMinutes(minute);
		mMinuteAdapter = new CalendarTextAdapter(getApplicationContext(),
				arry_minutes, currentMinute, maxTextSize, minTextSize);
		wvMinute.setVisibleItems(5);
		wvMinute.setViewAdapter(mMinuteAdapter);
		wvMinute.setCurrentItem(currentMinute);

		wvDate.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDateAdapter.getItemText(wheel
						.getCurrentItem());
				selectDate = wheel.getCurrentItem();
				setTextviewSize(currentText, mDateAdapter);
				// 动态添加
				if (oldValue > newValue) {
					if (newValue < 100) {
						// 需要添加前面一年的数据
						MIN_YEAR -= 1;
						arry_dates.addAll(0, getYaerDate(MIN_YEAR));
						selectDate += calDaysOfYear(MIN_YEAR);
						mDateAdapter = new CalendarTextAdapter(context,
								arry_dates, selectDate, maxTextSize,
								minTextSize);
						wvDate.setVisibleItems(5);
						wvDate.setViewAdapter(mDateAdapter);
						wvDate.setCurrentItem(selectDate);
					}
				} else {
					if (mDateAdapter.getItemsCount() - newValue < 100) {
						// 需要添加后面一年的数据
						MAX_YEAR += 1;
						arry_dates.addAll(mDateAdapter.getItemsCount(),
								getYaerDate(MAX_YEAR));
						mDateAdapter = new CalendarTextAdapter(context,
								arry_dates, selectDate, maxTextSize,
								minTextSize);
						wvDate.setVisibleItems(5);
						wvDate.setViewAdapter(mDateAdapter);
						wvDate.setCurrentItem(selectDate);
					}
				}
			}
		});

		wvDate.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mDateAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mDateAdapter);

				showReminderTime();
			}
		});

		wvHour.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mHourAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mHourAdapter);
				selectHour = currentText;
			}
		});

		wvHour.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {

				String currentText = (String) mHourAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mHourAdapter);

				showReminderTime();
			}
		});

		wvMinute.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinuteAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mMinuteAdapter);
				selectMinute = currentText;
			}
		});

		wvMinute.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinuteAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mMinuteAdapter);

				showReminderTime();

			}
		});

        repeatSp = getSharedPreferences("repeat", 0);
        editor = repeatSp.edit();

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            if (requestCode == REQUESTCODE) {
                repeatNum = data.getIntExtra("selectItem", 0);
                Log.d("cfb","onActivityResult==="+repeatNum);
//                reminderRepeatDetailTxt.setText(String.valueOf(repeatNum));
                }
        }
    }

    @SuppressLint("SimpleDateFormat")
	private void showReminderTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(MIN_YEAR, 0, 1);
		calendar.add(Calendar.DATE, selectDate);

		reminderTime = (new SimpleDateFormat("yyyy年M月d日 E")).format(calendar
				.getTime())
				+ " "
				+ String.format("%02d", Integer.parseInt(selectHour))
				+ ":"
				+ String.format("%02d", Integer.parseInt(selectMinute));
		reminderContextTime = (new SimpleDateFormat("yy/M/dE")).format(calendar
				.getTime())
				+ " "
				+ String.format("%02d", Integer.parseInt(selectHour))
				+ ":"
				+ String.format("%02d", Integer.parseInt(selectMinute));
		reminderTimeTv.setText(reminderTime);
		reminderTimeContentTv.setText(reminderContextTime);
		timeStamp = (new SimpleDateFormat("yyyy年M月d日")).format(calendar
				.getTime())+String.format("%02d", Integer.parseInt(selectHour))+"时"+String.format("%02d", Integer.parseInt(selectMinute))+"分"+"00秒";
//		Log.d("chenshichun", "timeStamp:::"+timeStamp+":::::::"/*+Utils.getTime(timeStamp)*/);

        endTime = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime())+ " " +
                String.format("%02d", Integer.parseInt(selectHour)) + ":" + String.format("%02d", Integer.parseInt(selectMinute))+":00";

        if(TextUtils.isEmpty(selectHour) || TextUtils.isEmpty(selectMinute)){
            selectHour = String.valueOf(calendarNowTime.get(Calendar.HOUR_OF_DAY));
            selectMinute = String.valueOf(calendarNowTime.get(Calendar.MINUTE));
        }
        Log.d("cfb", "calendarEndTime==" + selectHour + ":::" + selectMinute + "==========" + calendarEndTime + "" + calendarEndTime);
        calendarEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(selectHour));
        calendarEndTime.set(Calendar.MINUTE, Integer.parseInt(selectMinute));
        calendarEndTime.set(Calendar.SECOND, 0);
        calendarEndTime.set(Calendar.MILLISECOND, 0);
	}

	@SuppressLint("SimpleDateFormat")
	private String getTimeFormat(String format, Date date) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}

	/**
	 * 获取某一年的String数据
	 * 
	 * @param year
	 * @return
	 */
	private ArrayList<String> getYaerDate(int year) {
		ArrayList<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 0, 1);
		while (calendar.get(Calendar.YEAR) <= year) {
			final Date date = calendar.getTime();
			list.add(getTimeFormat("MM月dd日 EEE", date));
			calendar.add(Calendar.DATE, 1);
		}
		return list;
	}

	// 设置为当前时间
	public void initTime() {
		Calendar c = Calendar.getInstance();
		setTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
				c.get(Calendar.DAY_OF_MONTH), getCurrHour(), getCurrMinute());
	}

	public void initDates() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(MIN_YEAR, 0, 1);
		while (calendar.get(Calendar.YEAR) <= MAX_YEAR) {
			final Date date = calendar.getTime();
			arry_dates.add(getTimeFormat("MM月dd日 EEE", date));
			calendar.add(Calendar.DATE, 1);
		}
	}

	public void initHours(int hours) {
		arry_hours.clear();
		for (int i = 0; i < hours; i++) {
			if (i < 10) {
				arry_hours.add("0" + i);
			} else {
				arry_hours.add(i + "");
			}
		}
	}

	public void initMinutes(int minutes) {
		arry_minutes.clear();
		for (int i = 0; i < minutes; i++) {
			if (i < 10) {
				arry_minutes.add("0" + i);
			} else {
				arry_minutes.add(i + "");
			}
		}
	}

	public interface OnTimePickListener {
		public void onClick(int year, int month, int day, String hour,
                            String minute);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	public int getCurrHour() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public int getCurrMinute() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 设置日期-时间
	 * 
	 * @param year
	 * @param month
	 *            1-12
	 * @param day
	 *            1-31
	 */
	public void setTime(int year, int month, int day, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		this.currentDate = calendar.get(Calendar.DAY_OF_YEAR) - 1;
		if (month < 6) {
			MIN_YEAR = year - 1;
			MAX_YEAR = year;
			this.currentDate += calDaysOfYear(MIN_YEAR);
		} else {
			MIN_YEAR = year;
			MAX_YEAR = year + 1;
		}
		selectDate = currentDate;
		selectHour = hour + "";
		selectMinute = minute + "";
		issetdata = true;
		this.currentHour = hour;
		this.currentMinute = minute;
		this.hour = 24;
		this.minute = 60;
	}

	/**
	 * 设置日期
	 * 
	 * @param date
	 */
	public int setDate(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.set(MIN_YEAR, 1, 1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days)) + 1;

	}
	
	/**
	 * 计算每年多少天
	 * 
	 * @return
	 */
	public int calDaysOfYear(int year) {
		if (year % 4 == 0 && year % 100 != 0) {
			return 366;
		} else {
			return 365;
		}
	}

	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 * @param leayyear
	 */
	public int calDaysOfMonth(int year, int month) {
		int day = 0;
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				if (leayyear) {
					day = 29;
				} else {
					day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day = 30;
				break;
			}
		}
		return day;
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}

		@Override
		protected void notifyDataChangedEvent() {
			super.notifyDataChangedEvent();
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.reminder_time_rl:
			isShowReminderWheelView(isReminderTimeShow);
			isReminderTimeShow = !isReminderTimeShow;
			break;
		case R.id.done_btn:
			if (reminderTitleEt.length() != 0) {
                Log.d("cfb","onClick---Sql"+repeatNum+",,,,repeatTime==="+repeatTime);
				SQLHelper.createSql(getApplicationContext());

					SQLHelper.updateNewDetail(getApplicationContext(),
							currentPosition, reminderTitleEt.getText()
									.toString(), reminderTimeContentTv
									.getText().toString(), "", reminderNoteEt
									.getText().toString(), currentTableCount ,repeatNum, 0);

				Intent intent = new Intent();
				intent.setClass(DetailsActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
                Log.d("cfb","calendarNowTime.getTimeInMillis()==="+calendarNowTime.getTimeInMillis()+"---calendarEndTime.getTimeInMillis()=="+calendarEndTime.getTimeInMillis());
                if(Utils.isValidTime(calendarNowTime.getTimeInMillis(),calendarEndTime.getTimeInMillis())) {
                    if(repeatNum == 1){
                        Utils.singleReminder(DetailsActivity.this, getApplicationContext(), reminderTitleEt.getText().toString(), reminderNoteEt.getText().toString(), calendarEndTime.getTimeInMillis(),endTime);
                    }else{
                        Log.d("cfb","detail==="+reminderTitleEt.getText().toString()+"   "+reminderNoteEt.getText().toString());
                        Utils.alarmRepeatReminder(DetailsActivity.this,
                            getApplicationContext(), reminderTitleEt.getText().toString(), reminderNoteEt.getText().toString(), /*timeStamp*/calendarEndTime.getTimeInMillis(), repeatTime,endTime);
                    }
                }else{
                    //Toast.makeText(getApplicationContext(),"时间不对",Toast.LENGTH_LONG).show();
                }
                }
			break;
		default:
			break;
		}
	}

	private void isShowReminderWheelView(boolean isReminderTimeShow) {
		VDialogPicker.setVisibility(isReminderTimeShow ? View.GONE
				: View.VISIBLE);
		lyDialogPickerLineView.setVisibility(isReminderTimeShow ? View.GONE
				: View.VISIBLE);
		reminderTimePromptTv.setVisibility(isReminderTimeShow ? View.VISIBLE
				: View.GONE);
		reminderTimeContentTv.setVisibility(isReminderTimeShow ? View.VISIBLE
				: View.GONE);
		reminderTimeTv.setVisibility(isReminderTimeShow ? View.GONE
				: View.VISIBLE);
	}


	/*public static void setWindowStatusBarColor(Activity activity, int colorResId) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = activity.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(activity.getResources().getColor(
						colorResId));

				// 底部导航栏
				// window.setNavigationBarColor(activity.getResources().getColor(colorResId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	@Override
	protected void onResume() {
        super.onResume();

        switch (repeatNum){
            case 1:
                reminderRepeatDetailTxt.setText(R.string.repeat_never);
                //repeatTime = 0;
                break;
            case 2:
                reminderRepeatDetailTxt.setText(R.string.repeat_day);
                repeatTime = 24 * 60 * 60 * 1000;
                break;
            case 3:
                reminderRepeatDetailTxt.setText(R.string.repeat_week);
                repeatTime = 7 * 24 * 60 * 60 * 1000;
                break;
            case 4:
                reminderRepeatDetailTxt.setText(R.string.repeat_two_week);
                repeatTime = 2 * 7 * 24 * 60 * 60 * 1000;
                break;
            case 5:
                reminderRepeatDetailTxt.setText(R.string.repeat_month);
                repeatTime = 30 * 24 * 60 * 60 * 1000;
                break;
            case 6:
                reminderRepeatDetailTxt.setText(R.string.repeat_year);
                repeatTime = 365 * 30 * 24 * 60 * 60 * 1000;
                break;
        }

		SharedPreferences repeatSp= getSharedPreferences("repeat", 0);
        Intent intents=new Intent();
        intents.setAction("android.intent.action.UPDATE_STATUSBAR");
        intents.putExtra("status_bar_bg_color_index", 1);
        intents.putExtra("status_bar_font_white", false);
        sendBroadcast(intents);
	}

    //获列表名称
    private void showReminderListName(){

        reminderListTxt.setText(getString(R.string.table3));
    }
}
