package ru.vasic2000.newweather.dataBase;

import android.database.sqlite.SQLiteDatabase;

public class WeathersTable {
    private final static String TABLE_NAME = "Weathers";
    private final static String COLUMN_ID = "CityName";
    private final static String COLUMN_TEMP = "Temperaure";
    private final static String COLUMN_DETAILS = "Details";
    private final static String COLUMN_ICON = "Icon_ID";
    private final static String COLUMN_TIME = "Time";

    static void createTable(SQLiteDatabase database) {

    String SQL_request = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
            + " VARCHAR(45) NOT NULL, " + COLUMN_DETAILS + " VARCHAR(45) NOT NULL, "
            + COLUMN_TEMP + " DECIMAL(2,2) NOT NULL, " + COLUMN_ICON +" VARCHAR(45) NOT NULL, "
            + COLUMN_TIME + " BIGINT(19) NOT NULL, PRIMARY KEY (" + COLUMN_ID + "));";


        database.execSQL(SQL_request);
        //CREATE TABLE Notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, note INTEGER)
    }

    static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_ICON
                + " TEXT DEFAULT 'Default title'");
    }



}
