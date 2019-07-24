package com.example.one;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;

import static com.example.one.MainActivity.tabId;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Listy.db";
    public static final String TABLE_NAME = "Listy";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";
    public static final String COL3 = "ITEM2";
    public static final String COL4 = "ITEM3";

    private static final int DATABASE_VERSION = 6;

    public HashMap<String, Integer> map;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        map = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COL1 + "INTEGER PRIMARY KEY, " +
                COL2 + " TEXT," +
                COL3 + " TEXT," +
                COL4 + " TEXT);"
        );
        // db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //MainActivity m = new MainActivity();
        if(tabId == 1) {
            //contentValues.put(COL2, item);
            contentValues.put(COL2, item);
            map.put(item, map.size()+ 1 );
        }
        else if(tabId == 2) {
            contentValues.put(COL3, item);
            map.put(item, map.size()+ 1 );
        }
        else {
            contentValues.put(COL4, item);
            map.put(item, map.size()+ 1 );
        }

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data =  db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public boolean removeData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean ok = db.isOpen();
        //ContentValues contentValues = new ContentValues();
        Boolean rem;
        if(tabId == 1) {
            rem = db.delete(TABLE_NAME, COL2 + "=?", new String[]{item}) > 0;
            if(rem) {
                map.remove(item);
            }
            return rem;
        }
        else if(tabId == 2) {
            rem = db.delete(TABLE_NAME, COL3 + "=?", new String[]{item}) > 0;
            if(rem) {
                map.remove(item);
            }
            return rem;
        }
        else {
            rem = db.delete(TABLE_NAME, COL4 + "=?", new String[]{item}) > 0;
            if(rem) {
                map.remove(item);
            }
            return rem;
        }

        //long result = db.insert(TABLE_NAME, null, contentValues);
    }
}

