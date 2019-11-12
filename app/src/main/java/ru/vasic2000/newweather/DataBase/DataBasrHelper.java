package ru.vasic2000.newweather.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBasrHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weathers.db";
    private static final int DATABASE_VERSION = 1;

    public DataBasrHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WeatherTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        WeatherTable.onUpgrade(sqLiteDatabase);
    }
}
