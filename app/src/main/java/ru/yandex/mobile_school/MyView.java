package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Button;

/**
 * Created by hash on 07/04/2017.
 */

public class MyView extends android.support.v7.widget.AppCompatButton {

	public MyView(Context context){
		super(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d(MyApplication.TAG, "View.onAttachedToWindow");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(MyApplication.TAG, "View.onMeasure");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Log.d(MyApplication.TAG, "View.onLayout");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(MyApplication.TAG, "View.onDraw");
	}
}
