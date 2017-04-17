package com.qwik.user.restaurant_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AABDatabaseManager {
    Context context;
    private SQLiteDatabase db;
    private final String DB_NAME = "Qwikxl_database";
    private final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Qwikxl_table";
    private final String TABLE_ROW_ID = "id";
    private final String TABLE_ROW_ONE = "table_row_one";
    private final String TABLE_ROW_TWO = "table_row_two";
    private final String TABLE_ROW_THREE = "table_row_three";
    private final String TABLE_ROW_FOUR = "table_row_four";
    private final String TABLE_ROW_FIVE = "table_row_five";
    private final String TABLE_ROW_SIX = "table_row_six";
    private final String TABLE_ROW_SEVEN = "table_row_seven";
    private final String TABLE_ROW_EIGHT = "table_row_eight";
   static int item_number;
    public AABDatabaseManager(Context context) {
        this.context = context;
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public void addRow(String rowStringOne, String rowStringTwo, String rowStringThree, String rowStringFour, String rowStringFive,String rowStringsix,String rowStringseven,String rowStringeight ) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_ONE, rowStringOne);
        values.put(TABLE_ROW_TWO, rowStringTwo);
        values.put(TABLE_ROW_THREE, rowStringThree);
        values.put(TABLE_ROW_FOUR, rowStringFour);
        values.put(TABLE_ROW_FIVE, rowStringFive);
        values.put(TABLE_ROW_SIX, rowStringsix);
        values.put(TABLE_ROW_SEVEN, rowStringseven);
        values.put(TABLE_ROW_EIGHT, rowStringeight);
        db.insert(TABLE_NAME, null, values);
        item_number=item_number+Integer.parseInt(rowStringThree);
    }

    public void deleteRow(long rowID) {
        db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);

    }

    public void delete() {
        db.delete(TABLE_NAME, null, null);

    }

    public void updateRow(long rowID, String quant ,String total) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_THREE, quant);
        values.put(TABLE_ROW_SIX,total);
        //	values.put(TABLE_ROW_TWO, rowStringTwo);
        db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);
        item_number=item_number+Integer.parseInt(quant);
    }

    int itemnumber()
    {

            return item_number;

    }

    public void update_food(long rowID, String quant,String total, String comment,String stock) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ROW_THREE, quant);
        values.put(TABLE_ROW_SIX, total);
        values.put(TABLE_ROW_FIVE, comment);
        values.put(TABLE_ROW_SEVEN, stock);
        //	values.put(TABLE_ROW_TWO, rowStringTwo);
        db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);
        item_number=item_number+Integer.parseInt(quant);
    }

    public ArrayList<Object> getRowAsArray(long rowID) {
        ArrayList<Object> rowArray = new ArrayList<Object>();
        Cursor cursor;
        cursor = db.query
                (
                        TABLE_NAME,
                        new String[]{TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO, TABLE_ROW_THREE},
                        TABLE_ROW_ID + "=" + rowID,
                        null, null, null, null, null
                );
        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            do {
                rowArray.add(cursor.getLong(0));
                rowArray.add(cursor.getString(2));
                rowArray.add(cursor.getString(3));
                rowArray.add(cursor.getString(4));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return rowArray;
    }

    public ArrayList<ArrayList<Object>> getAllRowsAsArrays() {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        Cursor cursor;
        cursor = db.query(
                TABLE_NAME,
                new String[]{TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO, TABLE_ROW_THREE, TABLE_ROW_FOUR, TABLE_ROW_FIVE,TABLE_ROW_SIX,TABLE_ROW_SEVEN,TABLE_ROW_EIGHT},
                null, null, null, null, null
        );
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                ArrayList<Object> dataList = new ArrayList<Object>();

                dataList.add(cursor.getLong(0));
                dataList.add(cursor.getString(1));
                dataList.add(cursor.getString(2));
                dataList.add(cursor.getString(3));
                dataList.add(cursor.getString(4));
                dataList.add(cursor.getString(5));
                dataList.add(cursor.getString(6));
                dataList.add(cursor.getString(7));
                dataList.add(cursor.getString(8));

                dataArrays.add(dataList);
            }
            while (cursor.moveToNext());
        }
        return dataArrays;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = " create table " +
                    TABLE_NAME +
                    " (" +
                    TABLE_ROW_ID + " integer primary key autoincrement not null," +
                    TABLE_ROW_ONE + " text,"
                    + TABLE_ROW_TWO + " text," + TABLE_ROW_THREE + " text," + TABLE_ROW_FOUR + " text," + TABLE_ROW_FIVE + " text," +TABLE_ROW_SIX + " text," +TABLE_ROW_SEVEN + " text," +TABLE_ROW_EIGHT + " text" +
                    "); ";
            db.execSQL(newTableQueryString);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
            // OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE.
        }
    }
}