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

import ru.yandex.mobile_school.App;
import ru.yandex.mobile_school.model.db.BaseHelper;
import ru.yandex.mobile_school.model.db.NoteCursorWrapper;
import ru.yandex.mobile_school.model.db.DbSchema;

public class StorageModel {

    @Inject
    Context context;

    private static final String SHARED_PREFS_NAME = "data_storage_shared_prefs";
    private static final String SHARED_PREFS_USER = "data_storage_shared_user";
    private static final int DEFAULT_USER_ID = 223322;

    private SQLiteDatabase mDatabase;
    private BaseHelper mBaseHelper;
    private int mUserId = DEFAULT_USER_ID;
    private SharedPreferences mSharedPreferences;

    public StorageModel() {
        App.getComponent().inject(this);
        mBaseHelper = new BaseHelper(context);
        mDatabase = mBaseHelper.getWritableDatabase();
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        mUserId = mSharedPreferences.getInt(SHARED_PREFS_USER, DEFAULT_USER_ID);
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return notes;
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper itemCursor = queryNotes(
                DbSchema.NotesTable.Cols.ID + " = ?",
                new String[] {id.toString()}
        );
        if (itemCursor.getCount() == 0) {
            return null;
        }

        itemCursor.moveToFirst();
        Note note = itemCursor.getNote();
        itemCursor.close();
        return note;
    }

    public int updateNote(Note item) {
        return mBaseHelper.updateNote(item);
    }

    public int addNote(Note item) {
        return mBaseHelper.insertNote(item);
    }

    public int deleteNote(Note item) {
        return mBaseHelper.deleteNote(item);
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.NotesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NoteCursorWrapper(cursor);
    }

    public boolean exportNotes(String destination) {
        File targetFile = new File(destination);
        File parent = targetFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            return false;
        }
        JsonAdapter<List<Note>> adapter = getNotesJsonAdapter();
        String json = adapter.toJson(getNotes());
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

    public boolean importNotes(String source) {
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
        JsonAdapter<List<Note>> adapter = getNotesJsonAdapter();
        List<Note> items = null;
        try {
            items = adapter.fromJson(text.toString());
        } catch (IOException ignored) {
        }
        if (items != null) {
            mBaseHelper.clearNotes();
            for (int i = 0; i < items.size(); i++) {
                addNote(items.get(i));
            }
        }
        return true;
    }

    public void replaceNotes(ArrayList<Note> items) {
        mBaseHelper.clearNotes();
        for (Note item : items) {
            addNote(item);
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

    private JsonAdapter<List<Note>> getNotesJsonAdapter() {
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
