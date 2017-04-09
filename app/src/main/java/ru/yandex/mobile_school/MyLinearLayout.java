package ru.yandex.mobile_school;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

public class MyLinearLayout extends LinearLayout {

	private WeakReference<MyActivity> mActivity;

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = new WeakReference<>((MyActivity)context);
	}

	public MyLinearLayout(Context context) {
		super(context);
	}


	private void addToLog(String text) {
		mActivity.get().addToLog(text);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		addToLog("ViewGroup.onAttachedToWindow");
	}

	@Override
	public void onViewAdded(View child) {
		super.onViewAdded(child);
		addToLog("ViewGroup.onViewAdded");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		addToLog("ViewGroup.onLayout");
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void onViewRemoved(View child) {
		super.onViewRemoved(child);
		addToLog("ViewGroup.onViewRemoved");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		addToLog("ViewGroup.onDetachedFromWindow");
	}
}
