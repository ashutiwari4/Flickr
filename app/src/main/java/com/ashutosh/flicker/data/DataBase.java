package com.ashutosh.flicker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ashutosh on 6/3/17.
 */

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "letsplay.db";
    private static final int DATABASE_VERSION = 1;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PhotoProvider.Tables.PHOTO_TABLE + " ("
                + PhotoContract.PhotoColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PhotoContract.PhotoColumns.PREDICATE + " TEXT NOT NULL,"
                + PhotoContract.PhotoColumns.PHOTO_URL + " TEXT NOT NULL"
                + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PhotoProvider.Tables.PHOTO_TABLE);
        onCreate(db);
    }
}

