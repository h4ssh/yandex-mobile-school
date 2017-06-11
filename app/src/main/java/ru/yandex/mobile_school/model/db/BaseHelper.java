package ru.yandex.mobile_school.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.db.DbSchema.ColorsTable;

public class BaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 3;
	private static final String DATABASE_NAME = "ymsBase";
	private SQLiteDatabase mDatabase;

	public BaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDatabase = db;
		db.execSQL("create table " + ColorsTable.NAME + "("  +
				"_id integer primary key autoincrement, "  +
				ColorsTable.Cols.ID + " TEXT, " +
				ColorsTable.Cols.TITLE + " TEXT, " +
				ColorsTable.Cols.DESCRIPTION + " TEXT, " +
				ColorsTable.Cols.COLOR + " TEXT, " +
				ColorsTable.Cols.CREATED + " TEXT, " +
				ColorsTable.Cols.EDITED + " TEXT, " +
				ColorsTable.Cols.VIEWED + " TEXT, " +
				ColorsTable.Cols.SERVER_ID + " INTEGER" +
				")"
		);
	}

	public int insertColor(Note color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return (int) mDatabase.insert(ColorsTable.NAME, null, colorValues);
	}

	public int updateColor(Note color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return mDatabase.update(ColorsTable.NAME, colorValues, ColorsTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int deleteColor(Note color) {
		openForWriting();
		return mDatabase.delete(ColorsTable.NAME, ColorsTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int clearColors() {
		openForWriting();
		return mDatabase.delete(ColorsTable.NAME, null, null);
	}

	private ContentValues getContentValues(Note color) {
		ContentValues values = new ContentValues();
		values.put(ColorsTable.Cols.ID, color.getId().toString());
		values.put(ColorsTable.Cols.TITLE, color.getTitle());
		values.put(ColorsTable.Cols.DESCRIPTION, color.getDescription());
		values.put(ColorsTable.Cols.COLOR, color.getColorAsHexString());
		values.put(ColorsTable.Cols.CREATED, color.getCreated());
		values.put(ColorsTable.Cols.EDITED, color.getEdited());
		values.put(ColorsTable.Cols.VIEWED, color.getViewed());
		values.put(ColorsTable.Cols.SERVER_ID, color.getServerId());
		return values;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ColorsTable.NAME);
		onCreate(db);
	}

	private void openForWriting() {
		if (mDatabase == null) {
			mDatabase = getWritableDatabase();
		}
	}
}
