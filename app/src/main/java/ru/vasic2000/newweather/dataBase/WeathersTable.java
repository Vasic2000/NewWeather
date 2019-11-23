package ru.vasic2000.newweather.dataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class WeathersTable {
    private final static String TABLE_NAME = "Weathers";
    private final static String COLUMN_ID = "CityName";
    private final static String COLUMN_COUNTRY = "Country";
    private final static String COLUMN_TEMP = "Temperature";
    private final static String COLUMN_PRESS = "Pressure";
    private final static String COLUMN_HUMIDITY = "Humidity";
    private final static String COLUMN_DETAILS = "Details";
    private final static String COLUMN_ICON = "Icon_ID";
    private final static String COLUMN_SUNRISE = "Sunrise";
    private final static String COLUMN_SUNSET = "Sunset";
    private final static String COLUMN_TIME = "Time";

    public static void createTable(SQLiteDatabase database) {

        String SQL_request = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " VARCHAR(45) NOT NULL, "
                + COLUMN_COUNTRY + " VARCHAR(2) NOT NULL, "
                + COLUMN_TEMP + " FLOAT NOT NULL, "
                + COLUMN_PRESS + " FLOAT NOT NULL, "
                + COLUMN_HUMIDITY + " FLOAT NOT NULL, "
                + COLUMN_DETAILS + " VARCHAR(45) NOT NULL, "
                + COLUMN_ICON + " INT NOT NULL, "
                + COLUMN_SUNRISE + " BIGINT(32) NOT NULL, "
                + COLUMN_SUNSET + " BIGINT(32) NOT NULL, "
                + COLUMN_TIME + " BIGINT(32) NOT NULL, "
                + "PRIMARY KEY (" + COLUMN_ID + "));";

        database.execSQL(SQL_request);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.delete(TABLE_NAME, null, null);
        createTable(database);
    }

    public static void addInfo(String cityName,
                               String country,
                               float temp,
                               float pressure,
                               float humidity,
                               String details,
                               int iconID,
                               long sunrise,
                               long sunset,
                               long time,
                               SQLiteDatabase database) {

        if(!isCityInBase(cityName, database)) {
            String SQL_request = "INSERT INTO " + TABLE_NAME + " ("
                    + COLUMN_ID + ", "
                    + COLUMN_COUNTRY + ", "
                    + COLUMN_TEMP + ", "
                    + COLUMN_PRESS + ", "
                    + COLUMN_HUMIDITY + ", "
                    + COLUMN_DETAILS + ", "
                    + COLUMN_ICON + ", "
                    + COLUMN_SUNRISE + ", "
                    + COLUMN_SUNSET + ", "
                    + COLUMN_TIME + ") VALUES ('"
                    + cityName + "', '"
                    + country + "', "
                    + temp + ", "
                    + humidity + ", "
                    + pressure + ", '"
                    + details + "', "
                    + iconID + ", "
                    + sunrise + ", "
                    + sunset + ", "
                    + time + ");";

            database.execSQL(SQL_request);
        } else {
            String SQL_request ="UPDATE " + TABLE_NAME + " SET "
                    + COLUMN_TEMP + " = " + temp + ", "
                    + COLUMN_PRESS + " = " + pressure + ", "
                    + COLUMN_HUMIDITY + " = " + humidity + ", "
                    + COLUMN_DETAILS + " = '" + details + "', "
                    + COLUMN_ICON + " = " + iconID + ", "
                    + COLUMN_SUNRISE + " = " + sunrise + ", "
                    + COLUMN_SUNSET + " = " + sunset + ", "
                    + COLUMN_TIME + " = " + time
                    + " WHERE " + COLUMN_ID + " = '" + cityName + "';";

                    database.execSQL(SQL_request);
        }
    }

    public static boolean actualWeatherTime(String cityName, SQLiteDatabase database) {
        String SQL_Request = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + cityName + "';";

        Cursor cursor = database.rawQuery(SQL_Request, null);

        if(cursor.moveToFirst()) {
            long baseInfoTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
            Long newTime = System.currentTimeMillis();
            if (Math.abs(newTime - baseInfoTime) < 3600000) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isCityInBase(String cityName, SQLiteDatabase database) {
        String SQL_Request = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + cityName + "';";
        Cursor cursor = database.rawQuery(SQL_Request, null);

        if (cursor.moveToFirst()) {
            if(cursor.getString(0).equals(cityName)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getInfoFromSQL(String cityName, SQLiteDatabase database) {
        List<String> result = new ArrayList();
        String SQL_Request = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + cityName + "';";
        Cursor cursor = database.rawQuery(SQL_Request, null);

        if(cursor.moveToFirst()) {
            result.add(cursor.getString(0));
            result.add(cursor.getString(1));
            result.add(String.valueOf(cursor.getFloat(2)));
            result.add(String.valueOf(cursor.getFloat(3)));
            result.add(String.valueOf(cursor.getFloat(4)));
            result.add(cursor.getString(5));
            result.add(String.valueOf(cursor.getInt(6)));
            result.add(String.valueOf(cursor.getLong(7)));
            result.add(String.valueOf(cursor.getLong(8)));
            result.add(String.valueOf(cursor.getLong(9)));
        }
        return result;

    }
}
