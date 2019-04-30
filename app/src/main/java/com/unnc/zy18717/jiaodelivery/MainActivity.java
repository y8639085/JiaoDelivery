package com.unnc.zy18717.jiaodelivery;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    SimpleCursorAdapter dataAdapter;
    long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryContentProvider("ABS(`"+MyProviderContract.DISTANCE+"`) LIMIT 3");
    }

    public void queryContentProvider(String sortOrder) {

        String[] projection = new String[]{
                MyProviderContract._ID,
                MyProviderContract.PICKUP,
                MyProviderContract.DES,
                MyProviderContract.DISTANCE,
                MyProviderContract.PRICE,
                MyProviderContract.STATUS
        };

        int[] colResIds = new int[]{
                R.id.value1,
                R.id.value2,
                R.id.value3,
                R.id.value4,
                R.id.value5,
                R.id.value6
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.DELIVERIES_URI, projection, null, null, sortOrder);

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.db_item_layout,
                cursor,
                projection,
                colResIds,
                0);

        // put data into listview
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button setPrice = (Button)findViewById(R.id.setPrice);
                Button setStatus = (Button)findViewById(R.id.setStatus);
                final String id1 = String.valueOf(id);
                setPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Enter price");
                        final EditText price = (EditText)new EditText(MainActivity.this);
                        price.setSingleLine(true);
                        builder.setView(price);
                        builder.setNegativeButton("Cancel", null);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String price1 = price.getText().toString();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MyProviderContract.PRICE, "$"+price1);
                                String[] selectionArgs = new String[] {id1};
                                getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });


            }
        });
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
}