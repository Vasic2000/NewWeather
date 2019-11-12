package ru.vasic2000.newweather.DataBase;

import android.database.sqlite.SQLiteDatabase;

public class WeathersTable {
    private final static String TABLE_NAME = "Weathers";
    private final static String COLUMN_ID = "CityName";
    private final static String COLUMN_DETAILS = "Details";
    private final static String COLUMN_TEMP = "Temperaure";
    private final static String COLUMN_ICON = "Icon_ID";

    static void createTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DETAILS + " INTEGER);");
        //CREATE TABLE Notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, note INTEGER)
    }

    static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_ICON
                + " TEXT DEFAULT 'Default title'");
    }



}
