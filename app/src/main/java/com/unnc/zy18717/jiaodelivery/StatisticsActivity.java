package com.unnc.zy18717.jiaodelivery;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerAdapter dataAdapter;
    private RecyclerView recyclerView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String[] projection = new String[]{
                MyProviderContract._ID,
                MyProviderContract.DISTANCE,
                MyProviderContract.PRICE,
        };

        String[] selectionArgs = new String[] {"delivered"};

        cursor = getContentResolver().query(MyProviderContract.DELIVERIES_URI, projection, "status=?", selectionArgs, MyProviderContract._ID);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dataAdapter = new RecyclerAdapter(this, cursor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.update(cursor);

        double price = 0, distance = 0;

        if(cursor.moveToFirst()){
            do {
                StringBuffer sb1 = new StringBuffer(cursor.getString(cursor.getColumnIndex("price")));
                price += Double.parseDouble(sb1.deleteCharAt(0).toString());
                StringBuffer sb2 = new StringBuffer(cursor.getString(cursor.getColumnIndex("distance")));
                distance += Double.parseDouble(sb2.delete(sb2.length()-2, sb2.length()).toString());
            } while(cursor.moveToNext());
        }

        TextView numOfPrice = (TextView)findViewById(R.id.numOfPrice);
        TextView numOfDistance = (TextView)findViewById(R.id.numOfDistance);
        numOfPrice.setText("$" + String.valueOf(price));
        numOfDistance.setText(String.valueOf(distance) + "km");
    }
}