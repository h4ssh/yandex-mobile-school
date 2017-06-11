package ru.yandex.mobile_school.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

import java.util.Date;
import java.util.UUID;

import ru.yandex.mobile_school.model.dto.NoteDTO;
import ru.yandex.mobile_school.utils.DateUtils;

public class Note implements Parcelable {

	public static final int COLOR_MASK = 0xFFFFFF;
	private UUID mId;
	private @ColorInt int mColor;
	private String mTitle;
	private String mDescription;
	private String mCreated;
	private String mViewed;
	private String mEdited;
	private int mServerId;

	public Note() {
		mId = UUID.randomUUID();
		mColor = Color.WHITE;
		mTitle = "";
		mDescription = "";
		mCreated = DateUtils.getCurrentDateString();
		mEdited = DateUtils.getCurrentDateString();
		mViewed = DateUtils.getCurrentDateString();
		mServerId = 0;
	}

	public Note(@ColorInt int color, String title, String description) {
		this();
		mColor = color;
		if (title != null) {
			mTitle = title;
		}
		if (description != null) {
			mDescription = description;
		}
	}

	public Note(String id, int color, String title, String description,
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

	public Note(NoteDTO note) {
		mId = UUID.fromString(note.getExtra());
		mColor = Color.parseColor(note.getColor());
		mTitle = note.getTitle();
		mDescription = note.getDescription();
		mCreated = note.getCreated();
		mEdited = note.getEdited();
		mViewed = note.getViewed();
		mServerId = note.getId();
	}

	protected Note(Parcel in) {
		mId = UUID.fromString(in.readString());
		mColor = in.readInt();
		mTitle = in.readString();
		mDescription = in.readString();
		mCreated = in.readString();
		mEdited = in.readString();
		mViewed = in.readString();
		mServerId = in.readInt();
	}

	public void updateWith(Note item) {
		mColor = item.getColor();
		mTitle = item.getTitle();
		mDescription = item.getDescription();
		mCreated = item.getCreated();
		mEdited = item.getEdited();
		mViewed = item.getViewed();
		mServerId = item.mServerId;
	}

	public static final Creator<Note> CREATOR = new Creator<Note>() {
		@Override
		public Note createFromParcel(Parcel in) {
			return new Note(in);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
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
		return String.format("#%06X", (COLOR_MASK & mColor));
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

	public Date getCreatedDate() {
		return DateUtils.parseDateString(mCreated);
	}

	public Date getEditedDate() {
		return  DateUtils.parseDateString(mEdited);
	}

	public Date getViewedDate() {
		return DateUtils.parseDateString(mViewed);
	}

	public int getServerId() {
		return mServerId;
	}

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

	public NoteDTO toNoteDTO() {
		NoteDTO note = new NoteDTO();
		note.setId(mServerId);
		note.setColor(getColorAsHexString());
		note.setTitle(mTitle);
		note.setDescription(mDescription);
		note.setCreated(mCreated);
		note.setEdited(mEdited);
		note.setViewed(mViewed);
		note.setExtra(mId.toString());
		return note;
	}
}
