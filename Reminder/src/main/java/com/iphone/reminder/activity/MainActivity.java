package com.iphone.reminder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.iphone.reminder.R;
import com.iphone.reminder.adapter.MyApater;
import com.iphone.reminder.listview.ListViewCompat;
import com.iphone.reminder.sqlite.SQLHelper;
import com.iphone.reminder.util.Utils;
import com.iphone.reminder.view.ListStyleView;

public class MainActivity extends Activity {
	@SuppressLint("NewApi")
    ListStyleView listStyleViewOne, listStyleViewTwo, listStyleViewThree, listStyleViewFour;
	private LinearLayout mHeaderLl;
	public static ScrollView mScrollView;
	public boolean isHeaderShow = true;// 控制mScrollView是否可以滑动
	private int status = 0;// 0:列表一二都在上面 1：列表一在下面 2：列表二在下面
	public static MainActivity mainActivity;
	private View shelterView;// 处于status=0时，在表二下方放一层用来点击
	private EditText keywordsSearchEt;
	private ListViewCompat searchResultsListView;
	private MyApater mYAdpter;
	private Button cancelBtn;
    private ImageView addIv;
	private boolean isSearch = false;
    private RelativeLayout searchResultsRe;
    private RelativeLayout ll;
    private boolean isDeleteTableOne, isDeleteTableTwo, isDeleteTableThree, isDeleteTableFour;
    private SharedPreferences settingSP ,colorSp;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		init();
		
		listStyleViewOne.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                if (listStyleViewOne.isEditStatus) {

                } else {
				switch (status) {
				case 0:
                        translateAnimation(listStyleViewTwo.getY(), Utils.TO_Y_VALUE,
                                listStyleViewTwo, Utils.SEY_Y);
                        translateAnimation(listStyleViewThree.getY(), Utils.TO_Y_VALUE + Utils.BOTTOM_GAP,
                                listStyleViewThree, Utils.SEY_Y + Utils.BOTTOM_GAP);
                        translateAnimation(listStyleViewFour.getY(), Utils.TO_Y_VALUE + Utils.BOTTOM_GAP * 2,
                                listStyleViewFour, Utils.SEY_Y + Utils.BOTTOM_GAP * 2);
                        mHeaderLl.setVisibility(View.GONE);
                        shelterView.setVisibility(View.GONE);
					status = 2;
					break;
                        case 1:// 表二在最上面
                        if (!ListStyleView.isEditStatus) {
                            translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y,
                                    listStyleViewOne, 0);
                            translateAnimation(listStyleViewTwo.getY(), Utils.MARGIN_GAP,
                                    listStyleViewTwo, Utils.MARGIN_GAP);

                            translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y + Utils.MARGIN_GAP * 2,
                                    listStyleViewOne, Utils.MARGIN_GAP * 2);

                            mHeaderLl.setVisibility(View.VISIBLE);
//                                shelterView.setVisibility(View.VISIBLE);
					status = 0;
                            }
					break;
                        case 2:// 表一在最上面
                        if (!ListStyleView.isEditStatus) {
                            translateAnimation(listStyleViewTwo.getY(), -Utils.TO_Y_VALUE,
                                    listStyleViewTwo, Utils.MARGIN_GAP);

                            translateAnimation(listStyleViewThree.getY(), -(Utils.TO_Y_VALUE + Utils.BOTTOM_GAP),
                                    listStyleViewThree, Utils.MARGIN_GAP * 2);

                            translateAnimation(listStyleViewFour.getY(), -(Utils.TO_Y_VALUE + Utils.BOTTOM_GAP * 2),
                                    listStyleViewFour, Utils.MARGIN_GAP * 3);
                            mHeaderLl.setVisibility(View.VISIBLE);
//                                shelterView.setVisibility(View.VISIBLE);
					status = 0;
                            }
					break;
				default:
					break;
				}

				listStyleViewOne.isEditShow(status == 2 ? true : false);
			}
            }
		});

		listStyleViewTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                if (listStyleViewTwo.isEditStatus) {

                }else{
                    switch (status) {
                        case 0:
                            translateAnimation(listStyleViewOne.getY(), Utils.SEY_Y,
                                    listStyleViewOne, Utils.SEY_Y);
                            translateAnimation(listStyleViewTwo.getY(), -Utils.MARGIN_GAP,
                                    listStyleViewTwo, 0);
                            translateAnimation(listStyleViewThree.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP,
                                    listStyleViewThree, Utils.SEY_Y + Utils.BOTTOM_GAP);
                            translateAnimation(listStyleViewFour.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP * 2,
                                    listStyleViewFour, Utils.SEY_Y + Utils.BOTTOM_GAP * 2);
                            mHeaderLl.setVisibility(View.GONE);
                            shelterView.setVisibility(View.GONE);
                            status = 1;
                            break;
                        case 1:// 表二在最上面
                        if (!ListStyleView.isEditStatus) {
                            translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y,
                                    listStyleViewOne, 0);
                            translateAnimation(listStyleViewTwo.getY(), Utils.MARGIN_GAP,
                                    listStyleViewTwo, Utils.MARGIN_GAP);
                            translateAnimation(listStyleViewThree.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP),
                                    listStyleViewThree, Utils.MARGIN_GAP * 2);
                            translateAnimation(listStyleViewFour.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP * 2),
                                    listStyleViewFour, Utils.MARGIN_GAP * 3);
                            mHeaderLl.setVisibility(View.VISIBLE);
                            status = 0;
//                            shelterView.setVisibility(View.VISIBLE);
                            }
                            break;
                        case 2:// 表一在最上面
                            if (!ListStyleView.isEditStatus) {
                                translateAnimation(listStyleViewTwo.getY(), -Utils.TO_Y_VALUE,
                                        listStyleViewTwo, Utils.MARGIN_GAP);
                                mHeaderLl.setVisibility(View.VISIBLE);
//                            shelterView.setVisibility(View.VISIBLE);
                                status = 0;
                            }
                            break;
                        default:
                            break;
				}
				listStyleViewTwo.isEditShow(status == 1 ? true : false);
                }
			}
		});
		

        listStyleViewThree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (listStyleViewThree.isEditStatus) {

                } else {
                    switch (status) {
                        case 0:
                            translateAnimation(listStyleViewOne.getY(), Utils.SEY_Y,
                                    listStyleViewOne, Utils.SEY_Y);
                            translateAnimation(listStyleViewTwo.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP,
                                    listStyleViewTwo, Utils.SEY_Y + Utils.BOTTOM_GAP);
                            translateAnimation(listStyleViewThree.getY(), -Utils.MARGIN_GAP * 2,
                                    listStyleViewThree, 0);
                            translateAnimation(listStyleViewFour.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP * 2,
                                    listStyleViewFour, Utils.SEY_Y + Utils.BOTTOM_GAP * 2);
                            mHeaderLl.setVisibility(View.GONE);
                            shelterView.setVisibility(View.GONE);
                            status = 3;
                            break;
                        case 1:// 表二在最上面
                            if (!ListStyleView.isEditStatus) {
                                translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y,
                                        listStyleViewOne, 0);
                                translateAnimation(listStyleViewTwo.getY(), Utils.MARGIN_GAP,
                                        listStyleViewTwo, Utils.MARGIN_GAP);
                                translateAnimation(listStyleViewThree.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP),
                                        listStyleViewThree, Utils.MARGIN_GAP * 2);
                                mHeaderLl.setVisibility(View.VISIBLE);
                                status = 0;
//                                shelterView.setVisibility(View.VISIBLE);
                            }
                            break;
                        case 2:// 表一在最上面
                            if (!ListStyleView.isEditStatus) {
                                translateAnimation(listStyleViewTwo.getY(), -Utils.TO_Y_VALUE,
                                        listStyleViewTwo, Utils.MARGIN_GAP);
                                mHeaderLl.setVisibility(View.VISIBLE);
//                                shelterView.setVisibility(View.VISIBLE);
                                status = 0;
                            }
                            break;
                        case 3:// 表三在上面
                            translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y,
                                    listStyleViewOne, 0);
                            translateAnimation(listStyleViewTwo.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP),
                                    listStyleViewTwo, Utils.MARGIN_GAP);
                            translateAnimation(listStyleViewThree.getY(), Utils.MARGIN_GAP * 2,
                                    listStyleViewThree, Utils.MARGIN_GAP * 2);
                            translateAnimation(listStyleViewFour.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP * 2),
                                    listStyleViewFour, Utils.MARGIN_GAP * 3);
                            mHeaderLl.setVisibility(View.VISIBLE);
                            shelterView.setVisibility(View.GONE);
                            status = 0;
                            break;
                        default:
                            break;
                    }
                    listStyleViewThree.isEditShow(status == 3 ? true : false);
                }
            }
        });

        listStyleViewFour.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (listStyleViewFour.isEditStatus) {

                } else {
                    switch (status) {
                        case 0:
                            translateAnimation(listStyleViewOne.getY(), Utils.SEY_Y,
                                    listStyleViewOne, Utils.SEY_Y);
                            translateAnimation(listStyleViewTwo.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP,
                                    listStyleViewTwo, Utils.SEY_Y + Utils.BOTTOM_GAP);
                            translateAnimation(listStyleViewThree.getY(), Utils.SEY_Y + Utils.BOTTOM_GAP * 2,
                                    listStyleViewThree, Utils.SEY_Y + Utils.BOTTOM_GAP * 2);
                            translateAnimation(listStyleViewFour.getY(), -Utils.MARGIN_GAP * 3,
                                    listStyleViewFour, 0);
                            mHeaderLl.setVisibility(View.GONE);
                            shelterView.setVisibility(View.GONE);
                            status = 4;
                            break;

                        case 4:// 表四在上面
                            translateAnimation(listStyleViewOne.getY(), -Utils.SEY_Y,
                                    listStyleViewOne, 0);
                            translateAnimation(listStyleViewTwo.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP),
                                    listStyleViewTwo, Utils.MARGIN_GAP);
                            translateAnimation(listStyleViewThree.getY(), -(Utils.SEY_Y + Utils.BOTTOM_GAP * 2),
                                    listStyleViewThree, Utils.MARGIN_GAP * 2);
                            translateAnimation(listStyleViewFour.getY(), Utils.MARGIN_GAP * 3,
                                    listStyleViewFour, Utils.MARGIN_GAP * 3);
                            mHeaderLl.setVisibility(View.VISIBLE);
                            shelterView.setVisibility(View.GONE);
                            status = 0;
                            break;
                        default:
                            break;

                    }
                    listStyleViewFour.isEditShow(status == 4 ? true : false);
                }
            }
        });
		shelterView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                if (status == 0) {
                    translateAnimation(listStyleViewOne.getY(), Utils.SEY_Y,
                            listStyleViewOne, Utils.SEY_Y);
                    translateAnimation(listStyleViewTwo.getY(), -Utils.MARGIN_GAP,
                            listStyleViewTwo, 0);
					mHeaderLl.setVisibility(View.GONE);
					status = 1;
					shelterView.setVisibility(View.GONE);
					listStyleViewTwo.isEditShow(status == 1 ? true : false);
					
				}
			}
		});
		
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				float downY = 0;
				float moveY = 0;

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					moveY = event.getY() - downY;

					break;
				case MotionEvent.ACTION_CANCEL:

					break;
				}

				return true;// 禁止滑动

			}
		});
	}

	
	private void    init(){
		mainActivity = this;
        ll = (RelativeLayout) findViewById(R.id.viewObj);
		mScrollView = (ScrollView) findViewById(R.id.scorll_view);
		mHeaderLl = (LinearLayout) findViewById(R.id.header_ll);
		shelterView = (View) findViewById(R.id.shelter_view);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        addIv = (ImageView) findViewById(R.id.add_iv);

		searchResultsRe = (RelativeLayout) findViewById(R.id.search_results_re);
		
		searchResultsListView = (ListViewCompat) findViewById(R.id.search_results_listview);
		
		keywordsSearchEt = (EditText) findViewById(R.id.keywords_search_et);

		keywordsSearchEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					mScrollView.setVisibility(View.GONE);
					shelterView.setVisibility(View.GONE);
					searchResultsRe.setVisibility(View.VISIBLE);
                    addIv.setVisibility(View.GONE);
                    cancelBtn.setVisibility(View.VISIBLE);
					isSearch = true;
                    keywordsSearchEt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                    keywordsSearchEt.setPadding(10,0,0,0);
				}
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isSearch){// 取消
					mScrollView.setVisibility(View.VISIBLE);
//					shelterView.setVisibility(View.VISIBLE);
					searchResultsRe.setVisibility(View.GONE);
                    addIv.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.GONE);
					isSearch = false;
                    keywordsSearchEt.clearFocus();
                    keywordsSearchEt.setGravity(Gravity.CENTER);
                    keywordsSearchEt.setPadding(0,0,0,0);
                    keywordsSearchEt.setText("");
                    Utils.hideImm();
				}	
			}
		});
		
        addIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settingSP.getInt("TOTAL_TABLE", 2)<4) {
                    settingSP.edit().putInt("TOTAL_TABLE", settingSP.getInt("TOTAL_TABLE", 2) + 1).commit();
                    refresh();
                }
            }
        });
		keywordsSearchEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(keywordsSearchEt.getText().toString().length()!=0){
					searchResultsListView.setVisibility(View.VISIBLE);
					mYAdpter = new MyApater(getApplicationContext(),0);// 0表示搜索出來的数据
					mYAdpter.setmMessageItems(Utils.addSearchData(
							getApplicationContext(), keywordsSearchEt.getText().toString()));
					searchResultsListView.setAdapter(mYAdpter);
				}else{
					searchResultsListView.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
            }
        });

        settingSP = getSharedPreferences("title", 0);
        colorSp = getSharedPreferences("colors", 1);
        SharedPreferences.Editor settingSPEditor = settingSP.edit();
        SharedPreferences.Editor colorSpEditor = colorSp.edit();
        Log.d("chenshichun", "" + this.getClass().getCanonicalName() + ":::::::::::::::::::::"+settingSP.getInt("TOTAL_TABLE",2));
        // 表一
        if (!isDeleteTableOne) {
            listStyleViewOne = new ListStyleView(this, Utils.addDetailsDate(getApplicationContext(), 1), 1);
            listStyleViewOne.initDetailHead(settingSP.getString("TITLE" + 1, getApplicationContext().getResources().getString(R.string.table1)),
                    0xAA000000, getApplicationContext().getResources().getString(R.string.table1_no_project));
            ll.addView(listStyleViewOne);
        }
        // 表二
        if (!isDeleteTableTwo) {
            listStyleViewTwo = new ListStyleView(this, Utils.addDetailsDate(getApplicationContext(), 2), 2);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( // 表二初始位置往下调150px
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = Utils.MARGIN_GAP;

            ll.addView(listStyleViewTwo, layoutParams);

            listStyleViewTwo.initDetailHead(settingSP.getString("TITLE" + 2,
                    getApplicationContext().getResources().getString(R.string.reminder_pro).toString()), 0xAA7dd1f0, "");
            settingSPEditor.putString("TITLE" + 2,getApplicationContext().getResources().getString(R.string.table2).toString()).commit();
            colorSpEditor.putInt("CURRENT_TABLE" + 2, 0xFF7dd1f0);
        }
        // 表三
        if (!isDeleteTableThree) {
            listStyleViewThree = new ListStyleView(this, Utils.addDetailsDate(getApplicationContext(), 3), 3);
            RelativeLayout.LayoutParams layoutParamsThree = new RelativeLayout.LayoutParams( // 表二初始位置往下调150px
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParamsThree.topMargin = Utils.MARGIN_GAP * 2;
            if (settingSP.getInt("TOTAL_TABLE",2) > 2) {
                ll.addView(listStyleViewThree, layoutParamsThree);
            }
            listStyleViewThree.initDetailHead(settingSP.getString("TITLE" + 3, getApplicationContext().getResources().getString(R.string.table3).toString()), 0xAAd6e663, "");
            settingSPEditor.putString("TITLE" + 3,getApplicationContext().getResources().getString(R.string.table3).toString()).commit();
            colorSpEditor.putInt("CURRENT_TABLE" + 3, 0xFFcb72e0);
        }
        // 表四
        if (!isDeleteTableFour) {
            listStyleViewFour = new ListStyleView(this, Utils.addDetailsDate(getApplicationContext(), 4), 4);
            RelativeLayout.LayoutParams layoutParamsFour = new RelativeLayout.LayoutParams( // 表二初始位置往下调150px
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParamsFour.topMargin = Utils.MARGIN_GAP * 3;
            if (settingSP.getInt("TOTAL_TABLE",2) > 3) {
                ll.addView(listStyleViewFour, layoutParamsFour);
            }
            listStyleViewFour.initDetailHead(settingSP.getString("TITLE" + 4, getApplicationContext().getResources().getString(R.string.table4).toString()), 0xAAd6e663, "");
            settingSPEditor.putString("TITLE" + 4,getApplicationContext().getResources().getString(R.string.table4).toString()).commit();
            colorSpEditor.putInt("CURRENT_TABLE" + 4, 0xFFcb72e0);
        }
    }


    /**
     * @param currentTableCount 刪除某張表
     */
    public void deleteTable(int currentTableCount) {
        switch (currentTableCount) {
            case 1:
                ll.removeView(listStyleViewOne);
                isDeleteTableOne = true;
                break;
            case 2:
                ll.removeView(listStyleViewTwo);
                isDeleteTableTwo = true;
                break;
            case 3:
                ll.removeView(listStyleViewThree);
                isDeleteTableThree = true;
                break;
            case 4:
                ll.removeView(listStyleViewFour);
                isDeleteTableFour = true;
                break;
        }
        SQLHelper.deleteAll(getApplicationContext(), currentTableCount);
        settingSP.edit().putInt("TOTAL_TABLE",settingSP.getInt("TOTAL_TABLE",2)-1).commit();
        Log.d("chenshichun", "" + this.getClass().getCanonicalName() + ":::::::::::::::::::111::"+settingSP.getInt("TOTAL_TABLE",2));
        refresh();
    }

    /**
     * 刷新, 这种刷新方法，只有一个Activity实例。
     */
    public void refresh() {
        onCreate(null);
        status = 0;
    }

	private void translateAnimation(final float fromYValue,
			final float toYValue, final ListStyleView listStyleView , final int setY) {
		final TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
				toYValue);
		animation.setDuration(500);// 设置动画持续时间
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@SuppressLint("NewApi")
			@Override
			public void onAnimationEnd(Animation arg0) {
				/*int left = listStyleView.getLeft();
				int top = listStyleView.getTop() + (int) (toYValue);
				int width = listStyleView.getWidth();
				int height = listStyleView.getHeight();*/
				listStyleView.clearAnimation();
//				listStyleView.layout(0, top, left + width, top + height);
				listStyleView.setY(setY);
			}
		});
		listStyleView.startAnimation(animation);

	}

	@Override
	protected void onResume() {
        colorSp= getSharedPreferences("color", 0);
		int colorOne = colorSp.getInt("CURRENT_TABLE"+1, 0xaa000000);
		int colorTwo = colorSp.getInt("CURRENT_TABLE"+2, 0xff7dd1f0);
        int colorThree = colorSp.getInt("CURRENT_TABLE"+3, 0xffcb72e0);
        int colorFour = colorSp.getInt("CURRENT_TABLE"+4, 0xffcb72e0);
		
        listStyleViewOne.setTitleColor(colorOne);
		listStyleViewTwo.setTitleColor(colorTwo);
        listStyleViewThree.setTitleColor(colorThree);
        listStyleViewFour.setTitleColor(colorFour);
		
		super.onResume();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
			
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
