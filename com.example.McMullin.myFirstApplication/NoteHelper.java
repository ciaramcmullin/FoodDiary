package com.example.McMullin.myfirstapplication;

/**
 *
 * Created by Ciara McMullin on 1/4/2018
 * A class that creates new entries
 *
 */


        import android.content.Context;
        import android.content.ContentValues;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteDatabase;


public class NoteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "note.db";
    private static final int SCHEMA_VERSION = 1;

    public NoteHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, note TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String note) {

        ContentValues cv = new ContentValues();
        cv.put("note", note);

        getWritableDatabase().insert("Notes", "note", cv);

    }

    public void update(String id, String note) {
        ContentValues cv = new ContentValues();
        String[] args = {id};

        cv.put("note", note);

        getWritableDatabase().update("Notes", cv, "_id=?", args);

    }

    public void delete(String id) {

        getWritableDatabase().delete("Notes", "_id=?", new String[]{id});
    }


    public Cursor getAll() {
        return (getReadableDatabase()
                .rawQuery("SELECT _id, note FROM Notes",
                        null));
    }


    public String getNote(Cursor c) {
        return (c.getString(1));
    }

    public Cursor getById(String id) {
        String[] args = {id};

        return (getReadableDatabase()
                .rawQuery("SELECT _id, note FROM Notes WHERE _id=?",
                        args));
    }
}
