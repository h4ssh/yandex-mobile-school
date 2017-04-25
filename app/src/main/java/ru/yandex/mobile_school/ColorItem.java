package ru.yandex.mobile_school;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

class ColorItem implements Parcelable{

	private @ColorInt int mColor;
	private String mTitle;
	private String mDescription;

	public ColorItem() {
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

	protected ColorItem(Parcel in) {
		mColor = in.readInt();
		mTitle = in.readString();
		mDescription = in.readString();
	}

	public static final Creator<ColorItem> CREATOR = new Creator<ColorItem>() {
		@Override
		public ColorItem createFromParcel(Parcel in) {
			return new ColorItem(in);
		}

		@Override
		public ColorItem[] newArray(int size) {
			return new ColorItem[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mColor);
		dest.writeString(mTitle);
		dest.writeString(mDescription);
	}
}
