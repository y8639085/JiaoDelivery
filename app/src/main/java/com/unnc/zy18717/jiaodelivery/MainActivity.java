package com.unnc.zy18717.jiaodelivery;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);
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