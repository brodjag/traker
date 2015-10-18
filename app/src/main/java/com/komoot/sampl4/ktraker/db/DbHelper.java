package com.komoot.sampl4.ktraker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brodjag on 16.10.15.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 1;

    public static String TABLE_ROUTES="routes";
    public static String TABLE_POINTS="points";

    public static String TABLE_ROUTES_NAME="name";

    // запрос на создание базы данных
    private static final String TABLE_POINTS_CREATE = "CREATE TABLE points (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,route_id integer NOT NULL,langitude real,longitude real,cdate timestamp,image_url text); ";
    private static final String TABLE_ROUTES_CREATE=      "CREATE TABLE routes (id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,name text,cdate timestamp);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_POINTS_CREATE);
        database.execSQL(TABLE_ROUTES_CREATE);
       // fillData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    //    onCreate(database);
    }



    private void fillData(SQLiteDatabase db) {
       ContentValues values = new ContentValues();
        values.put("name", "route #1");
       db.insert(TABLE_ROUTES, null, values);
    }





}
