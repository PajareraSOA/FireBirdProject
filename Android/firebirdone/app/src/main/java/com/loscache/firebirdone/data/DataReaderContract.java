package com.loscache.firebirdone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by cdsac on 05/11/2017.
 */

public class DataReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataReaderContract() {}

    // Queries to create db
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataMeasurements.TABLE_NAME + " (" +
                    DataMeasurements._ID + " INTEGER PRIMARY KEY," +
                    DataMeasurements.COLUMN_NAME_TEMPERATURE + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_SMOKE + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_FLAME + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_FOOD + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_WATER + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    DataMeasurements.COLUMN_NAME_IS_HISTORY + TEXT_TYPE +" )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataMeasurements.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static class DataMeasurements implements BaseColumns {
        public static final String TABLE_NAME = "measurements";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_SMOKE = "smoke";
        public static final String COLUMN_NAME_FLAME = "flame";
        public static final String COLUMN_NAME_FOOD = "food";
        public static final String COLUMN_NAME_WATER = "water";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IS_HISTORY = "is_history";
    }



}
