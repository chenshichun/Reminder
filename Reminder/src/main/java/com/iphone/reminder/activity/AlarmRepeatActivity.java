package com.iphone.reminder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iphone.reminder.R;
import com.iphone.reminder.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class    AlarmRepeatActivity extends Activity {
	private Button backBtn;
	private ListView lv;
	private MyAdapter mAdapter;
	private int currentTabelCount;
	SharedPreferences repeatSp;
	SharedPreferences.Editor editor;
	private Integer alarmRepeat[] = new Integer[] { 1, 2, 3, 4, 5, 6 };
    private String repeatName[] = new String[]{};
    private int repeatNum,currentPosition;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_repeat_activity);
		//this.getActionBar().hide();

        repeatName = this.getResources().getStringArray(R.array.repeat_time);
		Intent intent = getIntent();
        repeatNum = intent.getIntExtra("repeatNum",1);//("repeatId",1);
		currentTabelCount = intent.getIntExtra("TABLE_COUNT", Utils.TABLE_COUNT_ONE);
        currentPosition = intent.getIntExtra("POSITION",1);
        Log.d("cfb---getintent","currentTabelCount=="+currentTabelCount+",,,currentPosition==="+currentPosition+"---repeatNum=="+repeatNum);

		repeatSp = getSharedPreferences("repeat", 0);
		editor = repeatSp.edit();
		lv = (ListView) findViewById(R.id.repeat_listview);
		mAdapter = new MyAdapter(this);
        mAdapter.setSelectItem(repeatNum-1);
		lv.setAdapter(mAdapter);

		backBtn = (Button) findViewById(R.id.cancel_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mAdapter.setSelectItem(arg2);
				mAdapter.notifyDataSetInvalidated();
				editor.putInt("ALARM_REPEAT", alarmRepeat[arg2]);
				editor.commit();
                Intent intent = new Intent();
                intent.putExtra("selectItem",alarmRepeat[arg2]);
                setResult(2, intent);
				finish();

			}
		});
	}

    @Override
    protected void onResume() {
        super.onResume();
        Intent intents=new Intent();
        intents.setAction("android.intent.action.UPDATE_STATUSBAR");
        intents.putExtra("status_bar_bg_color_index", 1);
        intents.putExtra("status_bar_font_white", false);
        sendBroadcast(intents);
    }

    /* 添加一个得到数据的方法，方便使用 */
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		/* 为动态数组添加数据 */
		for (int i = 0; i < 6; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("repeat_text", repeatName[i]);
			listItem.add(map);
		}

		return listItem;
	}

	// 新建一个类继承BaseAdapter，实现视图与数据的绑定
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return getData().size(); // 返回数组的长度
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

            repeatSp = getSharedPreferences("repeat", 0);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.alarm_repeat_item,
						null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */
				holder.repeatText = (TextView) convertView
						.findViewById(R.id.repeat_name);
				holder.choosedImg = (ImageView) convertView
						.findViewById(R.id.choosed_img);

				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
			}

			/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			holder.repeatText.setText(getData().get(position).get("repeat_text")
					.toString());
            if (position == selectItem) {
				holder.choosedImg
						.setImageResource(R.drawable.iphoneview_label_check_icon_blue);
			} else {
				holder.choosedImg.setImageResource(0);
			}
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;
	}

	/* 存放控件 的ViewHolder */
	public final class ViewHolder {
		public TextView repeatText;
		public ImageView choosedImg;
	}
}
