package com.Lists_App.Listy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import static com.Lists_App.Listy.MainActivity.tabId;

/*
 * Created by Osama Eldawebi
 * Last edited 2019-07-26
 * Utilizes an android SQLite database to store and write data
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "List.db";
    public static final String TABLE_NAME = "List";
    public static final String COL1 = "ID";
    public static final String List_1 = "ITEM1";
    public static final String List_2 = "ITEM2";
    public static final String List_3 = "ITEM3";

    private static final int DATABASE_VERSION = 8;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                List_1 + " TEXT," +
                List_2 + " TEXT," +
                List_3 + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds item to a specific list (corresponding to a column in the database)
     * depending on what tab is shown
     * @param item The item to be added
     * @return a boolean indicating if the item was successfully added or not
     */

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (tabId == 1) {
            contentValues.put(List_1, item);
        } else if (tabId == 2) {
            contentValues.put(List_2, item);
        } else {
            contentValues.put(List_3, item);
        }
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Receives the data stored in the database using a cursor object
     * @return a cursor object
     */
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    /**
     * Deletes item from a specific list (corresponding to a column in the database)
     * depending on what tab is shown
     * @param item The item to be deleted
     * @return a boolean indicating if the item was successfully deleted or not
     */
    public boolean removeData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        Boolean del;
        if (tabId == 1) {
            del = db.delete(TABLE_NAME, List_1 + "=?", new String[]{item}) > 0;
            if (del) {
            }
            return del;
        } else if (tabId == 2) {
            del = db.delete(TABLE_NAME, List_2 + "=?", new String[]{item}) > 0;
            if (del) {
            }
            return del;
        } else {
            del = db.delete(TABLE_NAME, List_3 + "=?", new String[]{item}) > 0;
            if (del) {
            }
            return del;
        }
    }

    /**
     * Replaces an existing item with a new one at corresponding row ID
     * @param oldItem The item to be replaced
     * @param newItem The item to replace existing item
     */
    public void updateItem(String oldItem, int id, String newItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        if (tabId == 1) {
            query = "UPDATE " + TABLE_NAME + " SET " + List_1 + " = '" +
                    newItem + "' WHERE " + COL1 + " = '" + id + "'" +
                    " AND " + List_1 + " = '" + oldItem + "'";
        } else if (tabId == 2) {
            query = "UPDATE " + TABLE_NAME + " SET " + List_2 + " = '" +
                    newItem + "' WHERE " + COL1 + " = '" + id + "'" +
                    " AND " + List_2 + " = '" + oldItem + "'";
        } else {
            query = "UPDATE " + TABLE_NAME + " SET " + List_3 + " = '" +
                    newItem + "' WHERE " + COL1 + " = '" + id + "'" +
                    " AND " + List_3 + " = '" + oldItem + "'";
        }
        db.execSQL(query);
    }

    /**
     * Returns a cursor over an item's corresponding ID values in the database
     * @param item The item whose ID is to be decided
     * @return a cursor object with ID values
     */
    public Cursor getItemID(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        if (tabId == 1) {
            query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                    " WHERE " + List_1 + " = '" + item + "'";
        } else if (tabId == 2) {
            query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                    " WHERE " + List_2 + " = '" + item + "'";
        } else {
            query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                    " WHERE " + List_3 + " = '" + item + "'";
        }
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}

