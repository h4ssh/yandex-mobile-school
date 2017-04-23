package ru.yandex.mobile_school;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * Created by hash on 23/04/2017.
 */

class ColorItem {

	private @ColorInt int mColor;
	private String mTitle;
	private String mDescription;

	private ColorItem() {
		mColor = Color.WHITE;
		mTitle = "";
		mDescription = "";
	}

	public ColorItem(@ColorInt int color, @Nullable String title, @Nullable String description) {
		this();
		mColor = color;
		if (title != null) mTitle = title;
		if (description != null) mDescription = description;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}
}
