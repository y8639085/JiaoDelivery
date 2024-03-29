package com.unnc.zy18717.jiaodelivery;
import android.net.Uri;

public class MyProviderContract {
    public static final String AUTHORITY = "com.unnc.zy18717.jiaodelivery.MyProvider";

    public static final Uri USERS_URI = Uri.parse("content://"+AUTHORITY+"/users");
    public static final Uri DELIVERIES_URI = Uri.parse("content://"+AUTHORITY+"/deliveries");
    public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

    public static final String _ID = "_id";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String PICKUP = "pickUp";
    public static final String DES = "des";
    public static final String DISTANCE = "distance";
    public static final String PRICE = "price";
    public static final String STATUS = "status";
    public static final String FINISHTIME = "finishTime";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/MyProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/MyProvider.data.text";
}