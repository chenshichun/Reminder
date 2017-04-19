package com.iphone.reminder.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iphone.reminder.R;
import com.iphone.reminder.activity.DetailsActivity;
import com.iphone.reminder.activity.MainActivity;
import com.iphone.reminder.data.MessageBean;
import com.iphone.reminder.listview.SlideView;
import com.iphone.reminder.listview.SlideView.OnSlideListener;
import com.iphone.reminder.sqlite.SQLHelper;
import com.iphone.reminder.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyApater extends BaseAdapter implements OnSlideListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MessageBean> mMessageItems = new ArrayList<MessageBean>();
    private SlideView mLastSlideViewWithStatusOn;
    private int tableCount = 1;
    private String repeatTextInList;

    public MyApater(Context context, int tableCount) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.tableCount = tableCount;
    }

    public void setmMessageItems(List<MessageBean> mMessageItems) {
        this.mMessageItems = mMessageItems;
    }

    @Override
    public int getCount() {
        if (mMessageItems == null) {
            mMessageItems = new ArrayList<MessageBean>();
        }
        return mMessageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.details_item, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(this);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        MessageBean item = mMessageItems.get(position);
        item.slideView = slideView;
        item.slideView.shrink();

        holder.title.setText(item.title);
        Log.d("cfb---m", "item.repeat==" + item.repeat);
        switch (item.repeat) {
            case 1:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_never);
                break;
            case 2:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_day);
                break;
            case 3:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_week);
                break;
            case 4:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_two_week);
                break;
            case 5:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_month);
                break;
            case 6:
                repeatTextInList = mContext.getResources().getString(R.string.repeat_year);
        }
        Log.d("cfb", "repeatTextInList===item.time====" + item.time + "~~~~holder.time.getText().toString()==" + holder.time.getText().toString());
        if (!TextUtils.isEmpty(item.time)) {
            holder.time.setText(item.time + ",  " + repeatTextInList);
        }
        holder.location.setText(item.location);
        holder.note.setText(item.note);
        holder.radioBtn.setChecked(item.isCheck);

        holder.title.setVisibility(TextUtils.isEmpty(holder.title.getText().toString()) ? View.GONE
                : View.VISIBLE);
        holder.time.setVisibility(TextUtils.isEmpty(item.time) ? View.GONE
                : View.VISIBLE);
        holder.location.setVisibility(TextUtils.isEmpty(holder.location.getText().toString()) ? View.GONE
                : View.VISIBLE);
        holder.note.setVisibility(TextUtils.isEmpty(holder.note.getText().toString()) ? View.GONE
                : View.VISIBLE);

        holder.radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!holder.radioBtn.isPressed())return;
                SQLHelper.updateIsCheckDetail(mContext, SQLHelper.queryAllMessage(mContext, tableCount).get(position).get("ID"), tableCount, b ? 1 : 0);
            }
        });

        holder.moreDetailsTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DetailsActivity.class);
                intent.putExtra("POSITION", SQLHelper.queryAllMessage(mContext, tableCount).get(position).get("ID"));
                intent.putExtra("DETAIL_TITLE", SQLHelper.queryAllMessage(mContext, tableCount).get(position).get("TITLE"));
                intent.putExtra("DETAIL_REPEAT", SQLHelper.queryAllMessage(mContext, tableCount).get(position).get("REPEAT"));
                intent.putExtra("TABLE_COUNT", tableCount);
                mContext.startActivity(intent);
                MainActivity.mainActivity.finish();
            }
        });

        holder.deleteTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableCount != 1) {
                    mMessageItems.remove(position);
                    notifyDataSetChanged();
                    SQLHelper.deleteDetailDate(mContext, SQLHelper.queryAllMessage(mContext, tableCount).get(position).get("ID"), tableCount);
                }
            }
        });

        return slideView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView time;
        public TextView location;
        public TextView note;
        public ViewGroup deleteHolder;
        public TextView moreDetailsTv;
        public TextView deleteTv;
        public CheckBox radioBtn;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.detail_title);
            time = (TextView) view.findViewById(R.id.detail_time);
            location = (TextView) view.findViewById(R.id.detail_location);
            note = (TextView) view.findViewById(R.id.detail_note);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
            moreDetailsTv = (TextView) deleteHolder
                    .findViewById(R.id.more_details);
            deleteTv = (TextView) deleteHolder.findViewById(R.id.delete);
            radioBtn = (CheckBox) view.findViewById(R.id.radio_btn);
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null
                && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }
}
