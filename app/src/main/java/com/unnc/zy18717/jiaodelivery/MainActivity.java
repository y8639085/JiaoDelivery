package com.unnc.zy18717.jiaodelivery;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerAdapter2 dataAdapter;
    long startTime = 0;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryContentProvider(MyProviderContract.DISTANCE + " LIMIT 3");
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
        dataAdapter = new RecyclerAdapter2(this, cursor);
        dataAdapter.setOnItemClickListener(new RecyclerAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                switch (view.getId()){
                    case R.id.setPrice:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Enter price");
                        final EditText price = (EditText)new EditText(MainActivity.this);
                        price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
                        final String[] status = new String[] {"pending", "pickd-up", "delivered"};
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
                                        contentValues.put(MyProviderContract.STATUS, status[index]);
                                        cursor.moveToPosition(position);
                                        final int d = cursor.getInt(cursor.getColumnIndex("_id"));
                                        String[] selectionArgs = new String[] {String.valueOf(d)};
                                        getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
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

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 1500) {
            Toast.makeText(MainActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryContentProvider(MyProviderContract.DISTANCE + " LIMIT 3");
    }
}