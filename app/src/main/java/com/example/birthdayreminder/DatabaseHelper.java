package com.example.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "birthdays.db";
    private static final int DATABASE_VERSION = 2;  // **Naikkan versi supaya database di-reset**


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE birthdays (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS birthdays");
        onCreate(db);
    }

    public void addBirthday(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);

        long result = db.insert("birthdays", null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Gagal menambahkan data!");
        } else {
            Log.d("DatabaseHelper", "Data berhasil ditambahkan: " + name + " - " + date);
        }

        db.close();
    }


    public Cursor getAllBirthdays() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _id, name, date FROM birthdays", null);
    }



    public void deleteBirthday(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("birthdays", "_id=?", new String[]{String.valueOf(id)});
        db.close();

        if (rowsDeleted > 0) {
            Log.d("DatabaseHelper", "Data berhasil dihapus: ID " + id);
        } else {
            Log.e("DatabaseHelper", "Gagal menghapus data: ID " + id);
        }
    }


    public void updateBirthday(int id, String newName, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("date", newDate);

        int rowsUpdated = db.update("birthdays", values, "_id=?", new String[]{String.valueOf(id)});
        db.close();

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Update berhasil: " + newName + " - " + newDate);
        } else {
            Log.e("DatabaseHelper", "Update GAGAL!");
        }
    }
}
