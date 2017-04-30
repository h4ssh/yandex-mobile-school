package ru.yandex.mobile_school.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by hash on 26/04/2017.
 */

public class LockableScrollView extends ScrollView {
	public LockableScrollView(Context context) {
		super(context);
	}

	public LockableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttrs) {
		super(context, attrs, defStyleAttrs);
	}

	private boolean mScrollable = true;

	public void setScrollingEnabled(boolean enabled) {
		mScrollable = enabled;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				return mScrollable && super.onTouchEvent(ev);
			default:
				return super.onTouchEvent(ev);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mScrollable && super.onInterceptTouchEvent(ev);
	}

}
