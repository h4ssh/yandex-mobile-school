package ru.yandex.mobile_school.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.yandex.mobile_school.model.Note;
import ru.yandex.mobile_school.model.db.DbSchema.NotesTable;

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
		db.execSQL("create table " + NotesTable.NAME + "("  +
				"_id integer primary key autoincrement, "  +
				NotesTable.Cols.ID + " TEXT, " +
				NotesTable.Cols.TITLE + " TEXT, " +
				NotesTable.Cols.DESCRIPTION + " TEXT, " +
				NotesTable.Cols.COLOR + " TEXT, " +
				NotesTable.Cols.CREATED + " TEXT, " +
				NotesTable.Cols.EDITED + " TEXT, " +
				NotesTable.Cols.VIEWED + " TEXT, " +
				NotesTable.Cols.SERVER_ID + " INTEGER" +
				")"
		);
	}

	public int insertColor(Note color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return (int) mDatabase.insert(NotesTable.NAME, null, colorValues);
	}

	public int updateColor(Note color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return mDatabase.update(NotesTable.NAME, colorValues, NotesTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int deleteColor(Note color) {
		openForWriting();
		return mDatabase.delete(NotesTable.NAME, NotesTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int clearColors() {
		openForWriting();
		return mDatabase.delete(NotesTable.NAME, null, null);
	}

	private ContentValues getContentValues(Note color) {
		ContentValues values = new ContentValues();
		values.put(NotesTable.Cols.ID, color.getId().toString());
		values.put(NotesTable.Cols.TITLE, color.getTitle());
		values.put(NotesTable.Cols.DESCRIPTION, color.getDescription());
		values.put(NotesTable.Cols.COLOR, color.getColorAsHexString());
		values.put(NotesTable.Cols.CREATED, color.getCreated());
		values.put(NotesTable.Cols.EDITED, color.getEdited());
		values.put(NotesTable.Cols.VIEWED, color.getViewed());
		values.put(NotesTable.Cols.SERVER_ID, color.getServerId());
		return values;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + NotesTable.NAME);
		onCreate(db);
	}

	private void openForWriting() {
		if (mDatabase == null) {
			mDatabase = getWritableDatabase();
		}
	}
}
