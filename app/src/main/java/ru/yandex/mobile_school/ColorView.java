package ru.yandex.mobile_school;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {

	private HSVColor mPrimaryColor;
	private HSVColor mCurrentColor;

	private float mMinimalHue;
	private float mMaximalHue;
	private static final float HUE_MAX_DELTA = 11.25f;

	public ColorView (Context context, AttributeSet atrs) {
		super(context, atrs);
		mPrimaryColor = new HSVColor(0, 1, 1);
		mMinimalHue = 0;
		mMaximalHue = 360;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView (Context context, float hue) {
		super(context);
		mPrimaryColor = new HSVColor(hue);
		mMaximalHue = hue + HUE_MAX_DELTA;
		mMinimalHue = hue - HUE_MAX_DELTA;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView (Context context, HSVColor color) {
		super(context);
		mPrimaryColor = color;
		mMinimalHue = 0;
		mMaximalHue = 360;
		setCurrentColor(mPrimaryColor);

	}

	public HSVColor getCurrentColor() {
		return mCurrentColor;
	}

	public void resetColor() {
		setCurrentColor(mPrimaryColor);
	}

	public void setCurrentColor(HSVColor color) {
		mCurrentColor = color;
		GradientDrawable backgroundShape = new GradientDrawable();
		backgroundShape.setColor(mCurrentColor.toRgb());
		backgroundShape.setStroke(3, Color.BLACK);
		setBackground(backgroundShape);
	}

	public void variateColor(float deltaHue, float deltaValue) {
		float currentHue = mCurrentColor.getHue();
		float newHue = currentHue + deltaHue;
		if (newHue > mMaximalHue) newHue = mMaximalHue;
		if (newHue < mMinimalHue) newHue = mMinimalHue;
		float currentValue = mCurrentColor.getValue();
		float newValue = currentValue + deltaValue;
		if (newValue > 1) newValue = 1;
		if (newValue < 0) newValue = 0;
		setCurrentColor(new HSVColor(newHue, newValue));
	}
}
