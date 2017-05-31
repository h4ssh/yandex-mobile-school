package ru.yandex.mobile_school.ui.views;

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
	private static final int HUE_MAX = 360;
	private static final int STROKE_WIDTH = 3;
	private static final int INDEX_HUE = 0;
	private static final int INDEX_SAT = 1;
	private static final int INDEX_VAL = 2;

	public ColorView(Context context, AttributeSet atrs) {
		super(context, atrs);
		mPrimaryColor = Color.WHITE;
		mMinimalHue = 0;
		mMaximalHue = HUE_MAX;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView(Context context, float hue) {
		super(context);
		mPrimaryColor = Color.HSVToColor(new float[] {hue, 1f, 1f});
		mMaximalHue = hue + HUE_MAX_DELTA;
		mMinimalHue = hue - HUE_MAX_DELTA;
		setCurrentColor(mPrimaryColor);
	}

	public ColorView(Context context, @ColorInt int color) {
		super(context);
		mPrimaryColor = color;
		mMinimalHue = 0;
		mMaximalHue = HUE_MAX;
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
		backgroundShape.setStroke(STROKE_WIDTH, Color.BLACK);
		setBackground(backgroundShape);
	}

	public void variateColor(float deltaHue, float deltaValue) {
		float[] hsv = new float[] {0, 0, 0};
		Color.colorToHSV(mCurrentColor, hsv);
		hsv[INDEX_HUE] += deltaHue;
		hsv[INDEX_SAT] = 1;
		if (hsv[INDEX_HUE] > mMaximalHue) {
			hsv[INDEX_HUE] = mMaximalHue;
		}
		if (hsv[INDEX_HUE] < mMinimalHue) {
			hsv[INDEX_HUE] = mMinimalHue;
		}
		hsv[INDEX_VAL] += deltaValue;
		if (hsv[INDEX_VAL] > 1) {
			hsv[INDEX_VAL] = 1;
		}
		if (hsv[INDEX_VAL] < 0) {
			hsv[INDEX_VAL] = 0;
		}
		setCurrentColor(Color.HSVToColor(hsv));
	}
}
