package ru.yandex.mobile_school;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

class HSVColor {
	private float mHue;
	private float mSaturation;
	private float mValue;

	HSVColor(float hue) {
		mHue = hue;
		mSaturation = 1.0f;
		mValue = 0.5f;
	}

	HSVColor(float hue, float value) {
		mHue = hue;
		mSaturation = 1.0f;
		mValue = value;
	}

	HSVColor(float hue, float saturation, float value) {
		mHue = hue;
		mSaturation = saturation;
		mValue = value;
	}

	float getHue() {
		return mHue;
	}

	float getSaturation() {
		return mSaturation;
	}

	float getValue() {
		return mValue;
	}

	int getRed() {
		return Color.red(toRgb());
	}

	int getGreen() {
		return Color.green(toRgb());
	}

	int getBlue() {
		return Color.blue(toRgb());
	}

	int toRgb() {
		return ColorUtils.HSLToColor(new float[] {mHue, mSaturation, mValue});
	}
}
