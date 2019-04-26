package com.unnc.zy18717.jiaodelivery;
import android.net.Uri;

public class MyProviderContract {
    public static final String AUTHORITY = "com.unnc.zy18717.jiaodelivery.MyProvider";

    public static final Uri USERS_URI = Uri.parse("content://"+AUTHORITY+"/users");
//    public static final Uri ANIMALS_URI = Uri.parse("content://"+AUTHORITY+"/animals");
//    public static final Uri FOOD_URI = Uri.parse("content://"+AUTHORITY+"/food");
    public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

    public static final String _ID = "_id";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
//    public static final String EMAIL = "email";
//    public static final String KIND = "kind";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/MyProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/MyProvider.data.text";
}