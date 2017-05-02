package ru.yandex.mobile_school.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public void exportColorItems() {
		String destination = "/storage/emulated/0/itemlist.ili";
		Moshi moshi = new Moshi.Builder()
				.add(new ColorItemJsonAdapter())
				.build();
		Type type = Types.newParameterizedType(List.class, ColorItem.class);
		JsonAdapter<List<ColorItem>> adapter = moshi.adapter(type);
		String json = adapter.toJson(getColorItems());
		try {
			FileWriter file = new FileWriter(destination);
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importColorItems() {
		String source = "/storage/emulated/0/itemlist.ili";
		File file = new File(source);
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line + '\n');
			}
		} catch (Exception ignored){
		} finally {
			try {
				br.close();
			} catch (IOException ignored) {}
		}
		Moshi moshi = new Moshi.Builder()
				.add(new ColorItemJsonAdapter())
				.build();
		Type type = Types.newParameterizedType(List.class, ColorItem.class);
		JsonAdapter<List<ColorItem>> adapter = moshi.adapter(type);
		List<ColorItem> items = null;
		try {
			items = adapter.fromJson(text.toString());
		} catch (IOException ignored) {}
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				addColorItem(items.get(i));
			}
		}

	}
}
