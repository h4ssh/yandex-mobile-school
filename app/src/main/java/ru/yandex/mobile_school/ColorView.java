package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {

	@ColorInt private final int mPrimaryColor;
	@ColorInt private int mCurrentColor;

	private final float mMinimalHue;
	private final float mMaximalHue;
	private static final float HUE_MAX_DELTA = 11.25f;

	public ColorView (Context context, AttributeSet atrs) {
		super(context, atrs);
		mPrimaryColor = Color.WHITE;
		mMinimalHue = 0;
		mMaximalHue = 360;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView (Context context, float hue) {
		super(context);
		mPrimaryColor = Color.HSVToColor(new float[]{hue, 1f, 1f});
		mMaximalHue = hue + HUE_MAX_DELTA;
		mMinimalHue = hue - HUE_MAX_DELTA;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView (Context context, @ColorInt int color) {
		super(context);
		mPrimaryColor = color;
		mMinimalHue = 0;
		mMaximalHue = 360;
		setCurrentColor(mPrimaryColor);

	}

	public @ColorInt int getCurrentColor() {
		return mCurrentColor;
	}

	public void resetColor() {
		setCurrentColor(mPrimaryColor);
	}

	public void setCurrentColor(@ColorInt int color) {
		mCurrentColor = color;
		GradientDrawable backgroundShape = new GradientDrawable();
		backgroundShape.setColor(mCurrentColor);
		backgroundShape.setStroke(3, Color.BLACK);
		setBackground(backgroundShape);
	}

	public void variateColor(float deltaHue, float deltaValue) {
		float [] hsv = new float[] {0,0,0};
		Color.colorToHSV(mCurrentColor, hsv);
		hsv[0] += deltaHue;
		hsv[1] = 1;
		if (hsv[0] > mMaximalHue) hsv[0] = mMaximalHue;
		if (hsv[0] < mMinimalHue) hsv[0] = mMinimalHue;
		hsv[2] += deltaValue;
		if (hsv[2] > 1) hsv[2] = 1;
		if (hsv[2] < 0) hsv[2] = 0;
		setCurrentColor(Color.HSVToColor(hsv));
	}
}
