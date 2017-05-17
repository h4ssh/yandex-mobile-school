package ru.yandex.mobile_school.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import ru.yandex.mobile_school.data.ColorItem;
import ru.yandex.mobile_school.db.DbSchema.ColorsTable;

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

		fillWithPreloadData();
	}

	private void fillWithPreloadData() {
		ColorItem crimson = new ColorItem(Color.parseColor("#DC143C"), "Crimson",
				"Crimson is a strong, red color, inclining to purple");
		ColorItem azure = new ColorItem(Color.parseColor("#007FFF"), "Azure",
				"Azure is a variation of blue that is often described as the color of the sky on a clear day");
		ColorItem aureolin = new ColorItem(Color.parseColor("#FDEE00"), "Aureolin",
				"Aureolin is a pigment sparingly used in oil and watercolor painting");
		ColorItem amethyst = new ColorItem(Color.parseColor("#9966CC"), "Amethyst",
				"amethyst is a moderate, transparent violet. Its name is derived from the stone amethyst, a form of quartz.");
		ColorItem charcoal = new ColorItem(Color.parseColor("#36454F"), "Charcoal",
				"Charcoal is a color that is a representation of the dark gray color of burned wood.");
		ColorItem jade = new ColorItem(Color.parseColor("#00A86B"), "Jade",
				"Jade, also called jade green is a representation of the color of the gemstone called jade, although the stone itself varies widely in hue.");

		insertColor(crimson);
		insertColor(azure);
		insertColor(aureolin);
		insertColor(amethyst);
		insertColor(charcoal);
		insertColor(jade);
	}

	public int insertColor(ColorItem color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return (int) mDatabase.insert(ColorsTable.NAME, null, colorValues);
	}

	public int updateColor(ColorItem color) {
		openForWriting();
		ContentValues colorValues = getContentValues(color);
		return mDatabase.update(ColorsTable.NAME, colorValues, ColorsTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int deleteColor(ColorItem color) {
		openForWriting();
		return mDatabase.delete(ColorsTable.NAME, ColorsTable.Cols.ID + " = ?",
				new String[] {color.getId().toString()});
	}

	public int clearColors() {
		openForWriting();
		return mDatabase.delete(ColorsTable.NAME, null, null);
	}

	private ContentValues getContentValues(ColorItem color) {
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
		if (mDatabase == null ) {
			mDatabase = getWritableDatabase();
		}
	}
}
