package ru.yandex.mobile_school.model.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Color;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.db.DbSchema.NotesTable;

public class ColorItemCursorWrapper extends CursorWrapper {

	public ColorItemCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public Note getColorItem() {
		String id = getString(getColumnIndex(NotesTable.Cols.ID));
		String title = getString(getColumnIndex(NotesTable.Cols.TITLE));
		String descr = getString(getColumnIndex(NotesTable.Cols.DESCRIPTION));
		String color = getString(getColumnIndex(NotesTable.Cols.COLOR));
		String created = getString(getColumnIndex(NotesTable.Cols.CREATED));
		String edited = getString(getColumnIndex(NotesTable.Cols.EDITED));
		String viewed = getString(getColumnIndex(NotesTable.Cols.VIEWED));
		int serverId = getInt(getColumnIndex(NotesTable.Cols.SERVER_ID));

		return new Note(id, Color.parseColor(color), title, descr,
				created, edited, viewed, serverId);
	}
}
