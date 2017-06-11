package ru.yandex.mobile_school.model;

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

import javax.inject.Inject;

import ru.yandex.mobile_school.model.db.BaseHelper;
import ru.yandex.mobile_school.model.db.ColorItemCursorWrapper;
import ru.yandex.mobile_school.model.db.DbSchema;
import ru.yandex.mobile_school.model.db.DbSchema.ColorsTable;

public class StorageModel {

	@Inject
	Context context;

	private static final String SHARED_PREFS_NAME = "data_storage_shared_prefs";
	private static final String SHARED_PREFS_USER = "data_storage_shared_user";
	private static final int DEFAULT_USER_ID = 223322;

	private static StorageModel sStorageModel;
	private SQLiteDatabase mDatabase;
	private BaseHelper mBaseHelper;
	private int mUserId = DEFAULT_USER_ID;
	private SharedPreferences mSharedPreferences;

	public static StorageModel get(Context context) {
		if (sStorageModel == null) {
			sStorageModel = new StorageModel(context);
		}
		return sStorageModel;
	}

	private StorageModel(Context context) {
		mBaseHelper = new BaseHelper(context.getApplicationContext());
		mDatabase = mBaseHelper.getWritableDatabase();
		mSharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		mUserId = mSharedPreferences.getInt(SHARED_PREFS_USER, DEFAULT_USER_ID);
	}

	public ArrayList<Note> getColorItems() {
		ArrayList<Note> notes = new ArrayList<>();

		ColorItemCursorWrapper cursor = queryColorItems(null, null);

		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				notes.add(cursor.getColorItem());
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}

		return notes;
	}

	public Note getColorItem(UUID id) {
		ColorItemCursorWrapper itemCursor = queryColorItems(
				ColorsTable.Cols.ID + " = ?",
				new String[] {id.toString()}
		);
		if (itemCursor.getCount() == 0) {
			return null;
		}

		itemCursor.moveToFirst();
		Note note = itemCursor.getColorItem();
		itemCursor.close();
		return note;
	}

	public int updateColorItem(Note item) {
		return mBaseHelper.updateColor(item);
	}

	public int addColorItem(Note item) {
		return mBaseHelper.insertColor(item);
	}

	public int deleteColorItem(Note item) {
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
		JsonAdapter<List<Note>> adapter = getColorsListJsonAdapter();
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
		JsonAdapter<List<Note>> adapter = getColorsListJsonAdapter();
		List<Note> items = null;
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

	public void replaceColorItems(ArrayList<Note> items) {
		mBaseHelper.clearColors();
		for (Note item : items) {
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

	private JsonAdapter<List<Note>> getColorsListJsonAdapter() {
		Moshi moshi = new Moshi.Builder()
				.add(new NoteJsonAdapter())
				.build();
		Type type = Types.newParameterizedType(List.class, Note.class);
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


