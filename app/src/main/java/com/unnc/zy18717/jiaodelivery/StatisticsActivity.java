package com.unnc.zy18717.jiaodelivery;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerAdapter dataAdapter;
    private RecyclerView recyclerView;
    private Cursor cursor;
    private EditText datePicker;
    private String my_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // set calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String day = String.valueOf(cal.get(Calendar.DATE));
        my_time = year + "/" + month + "/" + day;
        datePicker = (EditText) findViewById(R.id.datePicker);
        datePicker.setText(my_time);
        datePicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        datePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    showDatePickDlg();
            }
        });

        queryContentProvider(MyProviderContract._ID);
    }

    public void queryContentProvider(String sortOrder) {
        String[] projection = new String[]{
                MyProviderContract._ID,
                MyProviderContract.DISTANCE,
                MyProviderContract.PRICE,
        };

        String[] selectionArgs = new String[] {"delivered", datePicker.getText().toString()};

        cursor = getContentResolver().query(MyProviderContract.DELIVERIES_URI, projection, "status=? AND finishTime=?", selectionArgs, sortOrder);

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
                price += cursor.getDouble(cursor.getColumnIndex(MyProviderContract.PRICE));
                distance += cursor.getDouble(cursor.getColumnIndex(MyProviderContract.DISTANCE));
            } while(cursor.moveToNext());
        }

        TextView numOfPrice = (TextView)findViewById(R.id.numOfPrice);
        TextView numOfDistance = (TextView)findViewById(R.id.numOfDistance);
        numOfPrice.setText("$" + String.valueOf(price));
        numOfDistance.setText(String.valueOf(distance) + "km");
    }

    // show date in textview
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                datePicker.setText(year + "/" + (++monthOfYear) + "/" +dayOfMonth);
                queryContentProvider(MyProviderContract._ID);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}