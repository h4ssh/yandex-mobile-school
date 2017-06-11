package ru.yandex.mobile_school.model.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Color;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.db.DbSchema.ColorsTable;

public class ColorItemCursorWrapper extends CursorWrapper {

	public ColorItemCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public Note getColorItem() {
		String id = getString(getColumnIndex(ColorsTable.Cols.ID));
		String title = getString(getColumnIndex(ColorsTable.Cols.TITLE));
		String descr = getString(getColumnIndex(ColorsTable.Cols.DESCRIPTION));
		String color = getString(getColumnIndex(ColorsTable.Cols.COLOR));
		String created = getString(getColumnIndex(ColorsTable.Cols.CREATED));
		String edited = getString(getColumnIndex(ColorsTable.Cols.EDITED));
		String viewed = getString(getColumnIndex(ColorsTable.Cols.VIEWED));
		int serverId = getInt(getColumnIndex(ColorsTable.Cols.SERVER_ID));

		return new Note(id, Color.parseColor(color), title, descr,
				created, edited, viewed, serverId);
	}
}
