package com.iphone.reminder.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.iphone.reminder.R;
import com.iphone.reminder.activity.ChooseColorActivity;
import com.iphone.reminder.activity.MainActivity;
import com.iphone.reminder.activity.TestActivity;
import com.iphone.reminder.adapter.MyApater;
import com.iphone.reminder.data.MessageBean;
import com.iphone.reminder.listview.ListViewCompat;
import com.iphone.reminder.sqlite.SQLHelper;
import com.iphone.reminder.util.Utils;

import java.util.ArrayList;

public class ListStyleView extends LinearLayout {
	private ListViewCompat detailsList;
	public MyApater mYAdpter;
	private TextView mColorChooseTv, titleDescriptionTv;
	private View colorChooseLine;
	public static boolean isEditStatus = false;
	private boolean isShowDoneList = true;
	private ImageView footviewTv;
	private EditText mFootViewET;
	private int currentTableCount = 1;
	private boolean isAddNewDetailTitle = false;// 点击加号那一行加一个新的消息
	public Button mEditOrDoneButton, mDeleteListBt;
	public EditText listTitleEt;
    public TextView listTitleTv;
    public ImageView alarmIv;
    public TextView listCountTv;
    private SharedPreferences settingSP;

    public ListStyleView(final Context context,
			ArrayList<MessageBean> mMessageList, int tableCount) {// tableCount
																	// 1：表一 2：表二
		super(context);
		currentTableCount = tableCount;
		LayoutInflater.from(context).inflate(R.layout.list_style_item, this);
		detailsList = (ListViewCompat) findViewById(R.id.details_list);
		listTitleEt = (EditText) findViewById(R.id.list_title_et);
        listTitleTv =(TextView) findViewById(R.id.list_title_tv);
		listCountTv = (TextView) findViewById(R.id.list_count_tv);
		titleDescriptionTv = (TextView) findViewById(R.id.title_description_tv);
		mEditOrDoneButton = (Button) findViewById(R.id.edit_or_done);
		mColorChooseTv = (TextView) findViewById(R.id.color_choose_tv);
		colorChooseLine = (View) findViewById(R.id.color_choose_line);
		mDeleteListBt = (Button) findViewById(R.id.delete_list_tv);
        alarmIv = (ImageView) findViewById(R.id.alarm_iv);

        if(tableCount==1){
            alarmIv.setVisibility(View.VISIBLE);
            listCountTv.setVisibility(View.GONE);
            mEditOrDoneButton.setVisibility(View.GONE);
        }
		View footView = TestActivity.mTestActivity.getLayoutInflater().inflate(
				R.layout.item_footview, null);
		footviewTv = (ImageView) footView.findViewById(R.id.footview_iv);
		mFootViewET = (EditText) footView.findViewById(R.id.footview_et);

        settingSP = getContext().getSharedPreferences("title", 0);

		mFootViewET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (mFootViewET.getText().length() != 0) {
                    mEditOrDoneButton.setVisibility(View.VISIBLE);
					mEditOrDoneButton.setText(getContext().getResources().getString(R.string.done));
					isAddNewDetailTitle = true;
				} else {
                    if(currentTableCount!=1) {
                        mEditOrDoneButton.setText(getContext().getResources().getString(R.string.edit));
                    }
					isAddNewDetailTitle = false;
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

		mFootViewET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                mFootViewET.setFocusable(true);
				mEditOrDoneButton.setText(getContext().getResources().getString(R.string.done));
                if (mFootViewET.getText().length() != 0) {
                    isAddNewDetailTitle = true;
                }
			}
		});

		mColorChooseTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.mainActivity,
						ChooseColorActivity.class);
				intent.putExtra("TABLE_COUNT", currentTableCount);
				context.startActivity(intent);
			}
		});

		setListViewHeightBasedOnChildren(detailsList);
		mEditOrDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isAddNewDetailTitle) {// 新增加消息操作
                    if(mFootViewET.getText().toString().length()!=0){
					mFootViewET.clearFocus();
					mEditOrDoneButton.setText(getContext().getResources().getString(R.string.edit));
					SQLHelper.createSql(getContext());
					SQLHelper.insertSqlite(getContext(), mFootViewET.getText()
							.toString(), "", "", "", currentTableCount,1,0);
					mYAdpter.notifyDataSetChanged();

					mYAdpter.setmMessageItems(Utils.addDetailsDate(
							getContext(), currentTableCount));
					mYAdpter.notifyDataSetChanged();
					
					mFootViewET.setText("");
					listCountTv.setText(""+mYAdpter.getCount());
                    Utils.hideImm();
                    }else{
                        mEditOrDoneButton.setText(getContext().getResources().getString(R.string.edit));
                        Utils.hideImm();
                    }
				} else {
                    mFootViewET.clearFocus();
					editClick();
				}
			}
		});

		detailsList.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					MainActivity.mScrollView
							.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});

		mDeleteListBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isEditStatus) {
					if (isShowDoneList) {
						mDeleteListBt.setText(getContext().getResources().getString(R.string.hide_completed));
                        Utils.isShow = true;
					} else {
						mDeleteListBt.setText(getContext().getResources().getString(R.string.show_completed));
                        Utils.isShow = false;
					}
                    mYAdpter.setmMessageItems(Utils.addDetailsDate(
                            getContext(), currentTableCount));
                    mYAdpter.notifyDataSetChanged();
					isShowDoneList = !isShowDoneList;

				} else {
					// 删除列表操作
                    MainActivity.mainActivity.deleteTable(currentTableCount);
                    isEditStatus = !isEditStatus;
				}
			}
		});

		detailsList.addFooterView(footView);
		mYAdpter = new MyApater(getContext(), tableCount);
		detailsList.setAdapter(mYAdpter);

		mYAdpter.setmMessageItems(mMessageList);
		listCountTv.setText("" + mYAdpter.getCount());
	}

	public void initDetailHead(String listTitle, int listColor , String titleDescription) {
		listTitleEt.setText(listTitle);
		listTitleEt.setTextColor(listColor);
        listTitleTv.setText(listTitle);
        listTitleTv.setTextColor(listColor);

		listCountTv.setTextColor(listColor);
        titleDescriptionTv.setText(titleDescription);
        titleDescriptionTv.setTextColor(listColor);
	}

	public void isEditShow(boolean isEditShow) {
		if (isEditShow&&currentTableCount!=1) {
			mEditOrDoneButton.setVisibility(View.VISIBLE);
		} else {
			mEditOrDoneButton.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setTitleColor(int color){
		listTitleEt.setTextColor(color);
        listTitleTv.setTextColor(color);
		listCountTv.setTextColor(color);
	}
	
	public void editClick() {
		if (!isEditStatus) {
			mEditOrDoneButton.setText(getContext().getResources().getString(R.string.done));
			colorChooseLine.setVisibility(View.VISIBLE);
			mColorChooseTv.setVisibility(View.VISIBLE);
			mDeleteListBt.setText(getContext().getResources().getString(R.string.delete_list));
			mDeleteListBt.setTextColor(0xfffe3b30);
            listTitleEt.setVisibility(View.VISIBLE);
            Log.d("cfb","Utils.isHideOrShowImm(listTitleEt)=if="+Utils.isHideOrShowImm(listTitleEt));
            if(Utils.isHideOrShowImm(listTitleEt)){

            }
            listTitleTv.setVisibility(View.GONE);
            listTitleEt.setText(settingSP.getString("TITLE" + currentTableCount, ""));
        } else {
            Log.d("cfb","editClick()--else===="+listTitleEt.getText().toString());
            settingSP.edit().putString("TITLE"+currentTableCount, listTitleEt.getText().toString()).commit();

			mEditOrDoneButton.setText(getContext().getResources().getString(R.string.edit));
			colorChooseLine.setVisibility(View.GONE);
			mColorChooseTv.setVisibility(View.GONE);
			if (isShowDoneList) {
				mDeleteListBt.setText(getContext().getResources().getString(R.string.show_completed));
                Utils.isShow = true;
			} else {
				mDeleteListBt.setText(getContext().getResources().getString(R.string.hide_completed));
                Utils.isShow = false;
			}
            mDeleteListBt.setTextColor(0xff007afe);
            listTitleEt.setVisibility(View.GONE);
            listTitleTv.setVisibility(View.VISIBLE);
            listTitleTv.setText(settingSP.getString("TITLE"+currentTableCount,""));
            Log.d("cfb","Utils.isHideOrShowImm(listTitleEt)=else="+Utils.isHideOrShowImm(listTitleEt));
            if(Utils.isHideOrShowImm(listTitleEt)){
                Utils.hideImm();
            }


		}
		isEditStatus = !isEditStatus;
	}
	
	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
