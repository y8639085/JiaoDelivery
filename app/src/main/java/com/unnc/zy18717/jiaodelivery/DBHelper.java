package com.unnc.zy18717.jiaodelivery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        Log.d("ae3cw4", "DBHelper");
    }

    // create a new table to contain users, including 'id', 'username', 'password'
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ae3cw3", "onCreate");
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE users (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "username VARCHAR(128) NOT NULL," +
                "password VARCHAR(128) NOT NULL" +
                ");");
    }

    // this method can upgrade database version by override
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}