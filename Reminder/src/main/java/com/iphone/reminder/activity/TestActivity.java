package com.iphone.reminder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.iphone.reminder.R;
import com.iphone.reminder.listview.ListViewCompat;
import com.iphone.reminder.util.Utils;
import com.iphone.reminder.view.ListStyleView;

import java.util.ArrayList;

/**
 * Created by administrator on 17-4-19.
 */

public class TestActivity extends Activity implements View.OnClickListener {
    private LinearLayout headerLl;
    private EditText keywordsSearchEt;
    private ImageView addIv;
    private Button cancelBtn;
    private ScrollView scorllView;
    private RelativeLayout viewObj;
    private View shelterView;
    private RelativeLayout searchResultsRe;
    private ListViewCompat searchResultsListview;
    private RelativeLayout ll;
    public static TestActivity mTestActivity;
    private ArrayList<ListStyleView> mListStyleViewList = new ArrayList<ListStyleView>();
    private boolean isOneHead = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        mTestActivity = this;
        headerLl = (LinearLayout) findViewById(R.id.header_ll);
        keywordsSearchEt = (EditText) findViewById(R.id.keywords_search_et);
        addIv = (ImageView) findViewById(R.id.add_iv);
        addIv.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        scorllView = (ScrollView) findViewById(R.id.scorll_view);
        viewObj = (RelativeLayout) findViewById(R.id.viewObj);
        shelterView = (View) findViewById(R.id.shelter_view);
        searchResultsRe = (RelativeLayout) findViewById(R.id.search_results_re);
        searchResultsListview = (ListViewCompat) findViewById(R.id.search_results_listview);

        ll = (RelativeLayout) findViewById(R.id.viewObj);

        ListStyleView mListStyleView = new ListStyleView(getApplicationContext(), Utils.addDetailsDate(getApplicationContext(), mListStyleViewList.size()), mListStyleViewList.size());
        mListStyleView.initDetailHead("HEAD", 0xAA7dd1f0, "test");
        mListStyleViewList.add(mListStyleView);

        showListStyleViews();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_iv:

                ListStyleView mListStyleView = new ListStyleView(getApplicationContext(), Utils.addDetailsDate(getApplicationContext(), mListStyleViewList.size() + 1), mListStyleViewList.size() + 1);
                mListStyleViewList.add(mListStyleView);

                showListStyleViews();
                break;
        }
    }

    private void showListStyleViews() {

        ll.removeAllViews();
        ll.removeAllViewsInLayout();

        for (int i = 0; i < mListStyleViewList.size(); i++) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( // 表二初始位置往下调150px
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                mListStyleViewList.get(i).initDetailHead("HEAD", 0xAA7dd1f0, "test");
            } else {
                mListStyleViewList.get(i).initDetailHead("TEST", 0xAAd6e663, "test");
            }

            layoutParams.topMargin = Utils.MARGIN_GAP * (i);
            mListStyleViewList.get(i).setId(i);
            ll.addView(mListStyleViewList.get(i), layoutParams);
            mListStyleViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getApplicationContext(), "第" + view.getId() + "项", Toast.LENGTH_LONG).show();
                    if(!isOneHead) {

                        headerLl.setVisibility(View.GONE);

                        for (int i = 0; i < mListStyleViewList.size(); i++) {// 点击这项的后面item

                            if(i!=view.getId()) {

                                translateAnimation(mListStyleViewList.get(i), 1200 - Utils.MARGIN_GAP * i, 1200);

                            }else{

                                translateAnimation(mListStyleViewList.get(view.getId()), -Utils.MARGIN_GAP * view.getId(), 0);

                            }
                        }
                    }else{

                        headerLl.setVisibility(View.VISIBLE);

                        for (int i = 0; i < mListStyleViewList.size(); i++) {// 点击这项的后面item

                            if(i!=view.getId()) {

                                translateAnimation(mListStyleViewList.get(i), -(1200 - Utils.MARGIN_GAP * i), i*Utils.MARGIN_GAP);

                            }else{

                                translateAnimation(mListStyleViewList.get(view.getId()), Utils.MARGIN_GAP * view.getId(), Utils.MARGIN_GAP * view.getId());

                            }
                        }
                    }
                    isOneHead = !isOneHead;
                }
            });
        }
    }

    /*
    *
    * toYValue:移动距离
    * setY：最终的位置
    *
    * */

     private void translateAnimation(final ListStyleView listStyleView,
                                    final float toYValue, final int setY) {
        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
                toYValue);
        animation.setDuration(500);// 设置动画持续时间
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onAnimationEnd(Animation arg0) {
                listStyleView.clearAnimation();
                listStyleView.setY(setY);
            }
        });
        listStyleView.startAnimation(animation);

    }
}
