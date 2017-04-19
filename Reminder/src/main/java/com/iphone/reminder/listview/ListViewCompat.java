package com.iphone.reminder.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.iphone.reminder.activity.MainActivity;
import com.iphone.reminder.data.MessageBean;
import com.iphone.reminder.util.Utils;

public class ListViewCompat extends ListView {

	private static final String TAG = "ListViewCompat";

	private SlideView mFocusedItemView;

	public ListViewCompat(Context context) {
		super(context);
	}

	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float downY=0.0f;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			int x = (int) event.getX();
			int y = (int) event.getY();
			downY = event.getY();
			int position = pointToPosition(x, y);
			Log.e(TAG, "postion=" + position);
			if (position != INVALID_POSITION && position!=getCount()-1) {
				MessageBean data = (MessageBean) getItemAtPosition(position);
				mFocusedItemView = data.slideView;
				Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
			}
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			float moveY = Math.abs(event.getY()-downY);
			if(moveY>30 && Utils.isSoftShowing(MainActivity.mainActivity)){
                Utils.hideImm();
			}
		}
			break;
		default:
			break;
		}

		if (mFocusedItemView != null) {
			mFocusedItemView.onRequireTouchEvent(event);
		}

		return super.onTouchEvent(event);
	}

	
}
