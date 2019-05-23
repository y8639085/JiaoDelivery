package com.unnc.zy18717.jiaodelivery;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    RecyclerAdapter2 dataAdapter;
    MyReceiver receiver;
    long startTime = 0;
    int index;
    int orientation;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    public static final String CHANNEL_ID = "com.unnc.zy18717.jiaodelivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // adaptation of portrait or landscape
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main_landscape);

        queryContentProvider(MyProviderContract.DISTANCE + " LIMIT 3");

        sendBroadcast();

        setNotification();
    }

    public void queryContentProvider(final String sortOrder) {

        String[] projection = new String[]{
                MyProviderContract._ID,
                MyProviderContract.PICKUP,
                MyProviderContract.DES,
                MyProviderContract.DISTANCE,
                MyProviderContract.PRICE,
                MyProviderContract.STATUS
        };

        String[] selectionArgs = new String[] {"delivered"};

        final Cursor cursor = getContentResolver().query(MyProviderContract.DELIVERIES_URI, projection, "status!=?", selectionArgs, sortOrder);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dataAdapter = new RecyclerAdapter2(this, cursor, orientation);
        dataAdapter.setOnItemClickListener(new RecyclerAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                switch (view.getId()){
                    case R.id.setPrice:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Enter price");
                        final EditText price = (EditText)new EditText(MainActivity.this);
                        price.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        price.setSingleLine(true);
                        builder.setView(price);
                        builder.setNegativeButton("Cancel", null);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String price1 = price.getText().toString();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MyProviderContract.PRICE, price1);
                                cursor.moveToPosition(position);
                                final int d = cursor.getInt(cursor.getColumnIndex("_id"));
                                String[] selectionArgs = new String[]{String.valueOf(d)};
                                getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
                                queryContentProvider(sortOrder);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    case R.id.setStatus:
                        final String[] status = new String[] {"pending", "picked-up", "delivered"};
                        cursor.moveToPosition(position);
                        index = 0;
                        AlertDialog alertDialog2 = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Choose status")
                                .setIcon(R.mipmap.ic_launcher)
                                .setSingleChoiceItems(status, 0, new DialogInterface.OnClickListener() {//添加单选框
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        index = which;
                                    }
                                })
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        ContentValues contentValues = new ContentValues();
                                        final int d = cursor.getInt(cursor.getColumnIndex("_id"));
                                        String[] selectionArgs;
                                        if (status[index].equals("delivered")) {
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                                            String year = String.valueOf(cal.get(Calendar.YEAR));
                                            String month = String.valueOf(cal.get(Calendar.MONTH)+1);
                                            String day = String.valueOf(cal.get(Calendar.DATE));
                                            String my_time = year + "/" + month + "/" + day;
                                            selectionArgs = new String[]{String.valueOf(d)};
                                            contentValues.put(MyProviderContract.STATUS, status[index]);
                                            contentValues.put(MyProviderContract.FINISHTIME, my_time);
                                            getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
                                        } else {
                                            contentValues.put(MyProviderContract.STATUS, status[index]);
                                            selectionArgs = new String[]{String.valueOf(d)};
                                            getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
                                        }
                                        queryContentProvider(MyProviderContract.DISTANCE + " LIMIT 3");
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create();
                        alertDialog2.show();
                        break;
                    default:
                        break;
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.update(cursor);
    }

    public void viewAll (View view) {
        Intent intent = new Intent(this, AllItemsActivity.class);
        startActivity(intent);
    }

    public void statistics (View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void map(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void sendBroadcast() {
        receiver = new MyReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, itFilter);
//        unregisterReceiver(receiver);
    }

    private void setNotification() {
        createNotificationChannel();

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Jiao Delivery")
                .setContentText("Service on")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sequence Name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(MainActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {

            unregisterReceiver(receiver);
            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryContentProvider(MyProviderContract.DISTANCE + " LIMIT 3");
    }
}