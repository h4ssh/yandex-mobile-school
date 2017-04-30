package ru.yandex.mobile_school.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ru.yandex.mobile_school.db.BaseHelper;
import ru.yandex.mobile_school.db.ColorItemCursorWrapper;
import ru.yandex.mobile_school.db.DbSchema;
import ru.yandex.mobile_school.db.DbSchema.ColorsTable;

public class DataStorage {
	private static DataStorage sDataStorage;
	private SQLiteDatabase mDatabase;
	private BaseHelper mBaseHelper;

	public static DataStorage get(Context context) {
		if (sDataStorage == null) {
			sDataStorage = new DataStorage(context);
		}
		return sDataStorage;
	}

	private DataStorage(Context context) {
		mBaseHelper = new BaseHelper(context.getApplicationContext());
		mDatabase = mBaseHelper.getWritableDatabase();
	}

	public ArrayList<ColorItem> getColorItems() {
		ArrayList<ColorItem> colorItems = new ArrayList<>();

		ColorItemCursorWrapper cursor = queryColorItems(null, null);

		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				colorItems.add(cursor.getColorItem());
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}

		return colorItems;
	}

	public ColorItem getColorItem(int id) {
		ColorItemCursorWrapper itemCursor = queryColorItems(
				ColorsTable.Cols.ID + " = ?",
				new String[] {Integer.toString(id)}
		);
		if (itemCursor.getCount() == 0) return null;

		itemCursor.moveToFirst();
		ColorItem colorItem = itemCursor.getColorItem();
		itemCursor.close();
		return colorItem;
	}

	public int updateColorItem(ColorItem item) {
		return mBaseHelper.updateColor(item);
	}

	public int addColorItem(ColorItem item) {
		return mBaseHelper.insertColor(item);
	}

	private ColorItemCursorWrapper queryColorItems(String whereClause, String[] whereArgs) {
		Cursor cursor = mDatabase.query(
				DbSchema.ColorsTable.NAME,
				null,
				whereClause,
				whereArgs,
				null,
				null,
				null
		);
		return new ColorItemCursorWrapper(cursor);
	}
}
