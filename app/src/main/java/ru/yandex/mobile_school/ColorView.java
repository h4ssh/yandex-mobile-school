package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {

	@ColorInt private int mPrimaryColor;
	@ColorInt private int mCurrentColor;

	private float mMinimalHue;
	private float mMaximalHue;
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
		float newHue = hsv[0] + deltaHue;
		if (newHue > mMaximalHue) newHue = mMaximalHue;
		if (newHue < mMinimalHue) newHue = mMinimalHue;
		float currentValue = hsv[2];
		float newValue = currentValue + deltaValue;
		if (newValue > 1) newValue = 1;
		if (newValue < 0) newValue = 0;
		setCurrentColor(Color.HSVToColor(new float[] {newHue, 1f, newValue}));
	}
}
