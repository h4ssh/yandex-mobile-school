package ru.yandex.mobile_school;
import android.content.Context;

import java.lang.ref.WeakReference;


public class MyImageView extends android.support.v7.widget.AppCompatImageView {

	private WeakReference<MyActivity> mActivity;

	private void addToLog(String text) {
		mActivity.get().addToLog(text);
	}

	public MyImageView(Context context) {
		super(context);
		mActivity = new WeakReference<>((MyActivity) context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		addToLog("View.onAttachedToWindow");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		addToLog("View.onDetachedFromWindow");
	}


}


