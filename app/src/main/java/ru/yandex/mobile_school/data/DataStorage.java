package ru.yandex.mobile_school.data;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.List;
import java.util.UUID;

import ru.yandex.mobile_school.db.BaseHelper;
import ru.yandex.mobile_school.db.ColorItemCursorWrapper;
import ru.yandex.mobile_school.db.DbSchema;
import ru.yandex.mobile_school.db.DbSchema.ColorsTable;

public class DataStorage {

	private static final String SHARED_PREFS_NAME = "data_storage_shared_prefs";
	private static final String SHARED_PREFS_USER = "data_storage_shared_user";
	private static final int DEFAULT_USER_ID = 223322;

	private static DataStorage sDataStorage;
	private SQLiteDatabase mDatabase;
	private BaseHelper mBaseHelper;
	private int mUserId = DEFAULT_USER_ID;
	private SharedPreferences mSharedPreferences;

	public static DataStorage get(Context context) {
		if (sDataStorage == null) {
			sDataStorage = new DataStorage(context);
		}
		return sDataStorage;
	}

	private DataStorage(Context context) {
		mBaseHelper = new BaseHelper(context.getApplicationContext());
		mDatabase = mBaseHelper.getWritableDatabase();
		mSharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		mUserId = mSharedPreferences.getInt(SHARED_PREFS_USER, DEFAULT_USER_ID);
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

	public ColorItem getColorItem(UUID id) {
		ColorItemCursorWrapper itemCursor = queryColorItems(
				ColorsTable.Cols.ID + " = ?",
				new String[] {id.toString()}
		);
		if (itemCursor.getCount() == 0) {
			return null;
		}

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

	public int deleteColorItem(ColorItem item) {
		return mBaseHelper.deleteColor(item);
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

	public boolean exportColorItems(String destination) {
		File targetFile = new File(destination);
		File parent = targetFile.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) {
			return false;
		}
		JsonAdapter<List<ColorItem>> adapter = getColorsListJsonAdapter();
		String json = adapter.toJson(getColorItems());
		FileWriter file = null;
		try {
			file = new FileWriter(destination);
			file.write(json);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			safeCloseWriter(file);
		}
		return true;
	}

	private void safeCloseWriter(FileWriter file) {
		if (file != null) {
			try {
				file.close();
			} catch (IOException ignored) {
			}
		}
	}

	public boolean importColorItems(String source) {
		File file = new File(source);
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			saveCloseReader(br);
		}
		JsonAdapter<List<ColorItem>> adapter = getColorsListJsonAdapter();
		List<ColorItem> items = null;
		try {
			items = adapter.fromJson(text.toString());
		} catch (IOException ignored) {
		}
		if (items != null) {
			mBaseHelper.clearColors();
			for (int i = 0; i < items.size(); i++) {
				addColorItem(items.get(i));
			}
		}
		return true;
	}

	public void replaceColorItems(ArrayList<ColorItem> items) {
		mBaseHelper.clearColors();
		for (ColorItem item : items) {
			addColorItem(item);
		}
	}

	private void saveCloseReader(BufferedReader br) {
		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException ignored) {
		}
	}

	private JsonAdapter<List<ColorItem>> getColorsListJsonAdapter() {
		Moshi moshi = new Moshi.Builder()
				.add(new ColorItemJsonAdapter())
				.build();
		Type type = Types.newParameterizedType(List.class, ColorItem.class);
		return moshi.adapter(type);
	}

	public int getUserId() {
		return mUserId;
	}

	public void setUserId(int userId) {
		mSharedPreferences.edit().putInt(SHARED_PREFS_USER, userId).apply();
		mUserId = userId;
	}
}


