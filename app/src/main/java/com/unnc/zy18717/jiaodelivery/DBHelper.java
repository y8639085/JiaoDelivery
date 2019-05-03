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
                "distance DOUBLE NOT NULL," +
                "price DOUBLE NOT NULL," +
                "status VARCHAR(128) NOT NULL" +
                ");");

        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('Connecticut', 'Delaware', '10', '1.0', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('California', 'Florida', '9', '1.2', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('Indiana', 'Illinois', '8', '1.3', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('New hampshise111111111', 'New York', '18', '1.3', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '16', '1.6', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '19', '4.3', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '22', '1.3', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '23', '5.9', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '19', '7.4', 'pending');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '7', '9.0', 'delivered');");
        db.execSQL("INSERT INTO deliveries (pickUp, des, distance, price, status) VALUES ('702-1', '705-1', '2', '1.3', 'pending');");
    }

    // this method can upgrade database version by override
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS deliveries");
        onCreate(db);
    }
}