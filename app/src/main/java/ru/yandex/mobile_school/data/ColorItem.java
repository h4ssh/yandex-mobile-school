package ru.yandex.mobile_school.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

import ru.yandex.mobile_school.api.Note;
import ru.yandex.mobile_school.utils.DateUtils;

public class ColorItem implements Parcelable {

	private UUID mId;
	private @ColorInt int mColor;
	private String mTitle;
	private String mDescription;
	private String mCreated;
	private String mViewed;
	private String mEdited;
	private int mServerId;

	public ColorItem() {
		mId = UUID.randomUUID();
		mColor = Color.WHITE;
		mTitle = "";
		mDescription = "";
		mCreated = DateUtils.getCurrentDateString();
		mEdited = DateUtils.getCurrentDateString();
		mViewed = DateUtils.getCurrentDateString();
		mServerId = 0;
	}

	public ColorItem(@ColorInt int color, String title, String description) {
		this();
		mColor = color;
		if (title != null) mTitle = title;
		if (description != null) mDescription = description;
	}

	public ColorItem(String id, int color, String title, String description,
					 String created, String viewed, String edited, int serverId) {
		mId = UUID.fromString(id);
		mColor = color;
		mTitle = title;
		mDescription = description;
		mCreated = created;
		mViewed = viewed;
		mEdited = edited;
		mServerId = serverId;
	}

	public ColorItem(Note note) {
		mId = UUID.fromString(note.extra);
		mColor = Color.parseColor(note.color);
		mTitle = note.title;
		mDescription = note.description;
		mCreated = note.created;
		mEdited = note.edited;
		mViewed = note.viewed;
		mServerId = note.id;
	}

	protected ColorItem(Parcel in) {
		mId = UUID.fromString(in.readString());
		mColor = in.readInt();
		mTitle = in.readString();
		mDescription = in.readString();
		mCreated = in.readString();
		mEdited = in.readString();
		mViewed = in.readString();
		mServerId = in.readInt();
	}

	public void updateWith(ColorItem item) {
		mColor = item.getColor();
		mTitle = item.getTitle();
		mDescription = item.getDescription();
		mCreated = item.getCreated();
		mEdited = item.getEdited();
		mViewed = item.getViewed();
		mServerId = item.mServerId;
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

	public UUID getId() {
		return mId;
	}

	public void setColor(int color) {
		mColor = color;
		onEdit();
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
		onEdit();
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
		onEdit();
	}

	public String getColorAsHexString() {
		return String.format("#%06X", (0xFFFFFF & mColor));
	}

	public void setViewed() {
		mViewed = DateUtils.getCurrentDateString();
	}

	public String getCreated() {
		return mCreated;
	}

	public String getEdited() {
		return mEdited;
	}

	public String getViewed() {
		return mViewed;
	}

	public Date getCreatedDate() { return DateUtils.parseDateString(mCreated); }

	public Date getEditedDate() {return  DateUtils.parseDateString(mEdited);}

	public Date getViewedDate() { return DateUtils.parseDateString(mViewed);}

	public int getServerId() {return mServerId; }

	public void setServerId(int serverId) {
		mServerId = serverId;
	}

	private void onEdit() {
		mEdited = DateUtils.getCurrentDateString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId.toString());
		dest.writeInt(mColor);
		dest.writeString(mTitle);
		dest.writeString(mDescription);
		dest.writeString(mCreated);
		dest.writeString(mEdited);
		dest.writeString(mViewed);
		dest.writeInt(mServerId);
	}

	public Note toNote() {
		Note note = new Note();
		note.id = mServerId;
		note.color = getColorAsHexString();
		note.title = mTitle;
		note.description = mDescription;
		note.created = mCreated;
		note.edited = mEdited;
		note.viewed = mViewed;
		note.extra = mId.toString();
		return note;
	}
}
