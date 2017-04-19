package com.iphone.reminder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ChooseColorActivity extends Activity {
    private Button cancelBtn;
    private ListView lv;
    private MyAdapter mAdapter;
    private int currentTabelCount;
    SharedPreferences colorSp;
    SharedPreferences.Editor editor;
    private Integer color[] = new Integer[]{0xaacb72e0, 0xaa62d938, 0xaa1aacf7, 0xaae9ba00, 0xaaa1835d, 0xaafe2967, 0xaafe9400};
    private int colorOne, colorTwo;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_color_activity);
        //this.getActionBar().hide();

        Intent intent = getIntent();
        currentTabelCount = intent.getIntExtra("TABLE_COUNT", Utils.TABLE_COUNT_ONE);


        colorSp = getSharedPreferences("color", 0);
        editor = colorSp.edit();

        lv = (ListView) findViewById(R.id.color_listview);
        mAdapter = new MyAdapter(this);
        lv.setAdapter(mAdapter);

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {

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
                editor.putInt("CURRENT_TABLE" + currentTabelCount, color[arg2]);
                editor.commit();
                finish();

            }
        });
    }

    /* 添加一个得到数据的方法，方便使用 */
    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        String color[] = new String[]{getResources().getString(R.string.purple), getResources().getString(R.string.green), getResources().getString(R.string.blue), getResources().getString(R.string.yellow),
                getResources().getString(R.string.gray), getResources().getString(R.string.red), getResources().getString(R.string.orange)};
        int drawable[] = new int[]{R.drawable.purple, R.drawable.green, R.drawable.blue, R.drawable.yellow, R.drawable.gray, R.drawable.red, R.drawable.orange};

		/* 为动态数组添加数据 */
        for (int i = 0; i < 7; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("color_img", drawable[i]);
            map.put("color_text", color[i]);
            listItem.add(map);
        }

        return listItem;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intents = new Intent();
        intents.setAction("android.intent.action.UPDATE_STATUSBAR");
        intents.putExtra("status_bar_bg_color_index", 1);
        intents.putExtra("status_bar_font_white", false);
        sendBroadcast(intents);
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

            colorOne = colorSp.getInt("CURRENT_TABEL" + Utils.TABLE_COUNT_ONE, 0xaacb72e0);
            colorTwo = colorSp.getInt("CURRENT_TABEL" + Utils.TABLE_COUNT_TWO, 0xaacb72e0);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.choose_color_item,
                        null);
                holder = new ViewHolder();
                /* 得到各个控件的对象 */
                holder.colorText = (TextView) convertView
                        .findViewById(R.id.repeat_name);
                holder.colorImg = (ImageView) convertView
                        .findViewById(R.id.color_img);
                holder.choosedImg = (ImageView) convertView
                        .findViewById(R.id.choosed_img);

                convertView.setTag(holder); // 绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
            }

			/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
            holder.colorText.setText(getData().get(position).get("color_text")
                    .toString());
            holder.colorImg.setImageResource((Integer) getData().get(position)
                    .get("color_img"));
            if (currentTabelCount == Utils.TABLE_COUNT_ONE) {
                for (int i = 0; i < 7; i++) {
                    if (color[i] == colorOne) {
                        selectItem = i;
                    } else {
                    }
                }
            } else if (currentTabelCount == Utils.TABLE_COUNT_TWO) {
                for (int i = 0; i < 7; i++) {
                    if (color[i] == colorTwo) {
                        selectItem = i;
                    } else {
                    }
                }
            }
            notifyDataSetChanged();
            if (position == selectItem) {
                holder.choosedImg.setImageResource(R.drawable.iphoneview_label_check_icon_blue);
            } else {
                holder.choosedImg.setImageResource(0);
            }
            notifyDataSetChanged();
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
        public ImageView colorImg;
        public TextView colorText;
        public ImageView choosedImg;
    }
}
