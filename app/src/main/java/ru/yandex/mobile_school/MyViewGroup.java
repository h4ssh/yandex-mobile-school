package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by hash on 07/04/2017.
 */

public class MyViewGroup extends LinearLayout {

	public MyViewGroup(Context context) {
		super(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d(MyApplication.TAG, "ViewGroup.onAttachedToWindow");
	}

	@Override
	public void onViewAdded(View child) {
		super.onViewAdded(child);
		Log.d(MyApplication.TAG, "ViewGroup.onViewAdded");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(MyApplication.TAG, "ViewGroup.onMeasure");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Log.d(MyApplication.TAG, "ViewGroup.onLayout");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(MyApplication.TAG, "ViewGroup.onDraw");
	}

	@Override
	public void onViewRemoved(View child) {
		super.onViewRemoved(child);
		Log.d(MyApplication.TAG, "ViewGroup.onViewRemoved");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d(MyApplication.TAG, "ViewGroup.onDetachedFromWindow");
	}
}
