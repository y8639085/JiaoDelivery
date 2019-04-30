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
        Log.d("ae3cw4", "onCreate");
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE users (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "username VARCHAR(128) NOT NULL," +
                "password VARCHAR(128) NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE deliveries (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "pickUp VARCHAR(128) NOT NULL," +
                "des VARCHAR(128) NOT NULL," +
                "distance VARCHAR(128) NOT NULL," +
                "price VARCHAR(128) NOT NULL," +
                "status VARCHAR(128) NOT NULL" +
                ");");

        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('704-1', '706-1', '10km', '$1.0', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('701-1', '703-1', '9km', '$1.2', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '8km', '$2.3', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '18km', '$3.3', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '16km', '$1.6', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '19km', '$4.3', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '22km', '$1.3', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '23km', '$5.9', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '19km', '$7.4', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '7km', '$9.0', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '2km', '$1.3', 'pending');");
    }

    // this method can upgrade database version by override
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS deliveries");
        onCreate(db);
    }
}