package com.unnc.zy18717.jiaodelivery;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class AllItemsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    RecyclerAdapter2 dataAdapter;
    int index;
    String radioFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allitems);

        initRadio();
        queryContentProvider(null);
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

        final Cursor cursor = getContentResolver().query(MyProviderContract.DELIVERIES_URI, projection, null, null, sortOrder);

        // put data into recyclerview
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dataAdapter = new RecyclerAdapter2(this, cursor, 0);
        dataAdapter.setOnItemClickListener(new RecyclerAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                switch (view.getId()) {
                    case R.id.setPrice:
                        cursor.moveToPosition(position);
                        if (cursor.getString(cursor.getColumnIndex("status")).equals("delivered")) {
                            Toast.makeText(AllItemsActivity.this, "Cannot change price!", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AllItemsActivity.this);
                            builder.setTitle("Enter price");
                            final EditText price = (EditText) new EditText(AllItemsActivity.this);
                            price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            price.setSingleLine(true);
                            builder.setView(price);
                            builder.setNegativeButton("Cancel", null);
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String price1 = price.getText().toString();
                                    if (price1.length() == 0) {
                                        Toast.makeText(AllItemsActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(MyProviderContract.PRICE, price1);

                                        final int d = cursor.getInt(cursor.getColumnIndex("_id"));
                                        String[] selectionArgs = new String[]{String.valueOf(d)};
                                        getContentResolver().update(MyProviderContract.DELIVERIES_URI, contentValues, "_id=?", selectionArgs);
                                        queryContentProvider(sortOrder);
                                    }
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            break;
                        }
                    case R.id.setStatus:
                        final String[] status = new String[] {"pending", "picked-up", "delivered"};
                        cursor.moveToPosition(position);
                        if (cursor.getString(cursor.getColumnIndex("status")).equals("delivered")) {
                            Toast.makeText(AllItemsActivity.this, "Cannot change status!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            index = 0;
                            AlertDialog alertDialog2 = new AlertDialog.Builder(AllItemsActivity.this)
                                    .setTitle("Choose status")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setSingleChoiceItems(status, 0, new DialogInterface.OnClickListener() {//添加单选框
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            index = which;
                                        }
                                    })
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
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
                                            queryContentProvider(radioFlag);
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create();
                            alertDialog2.show();
                            break;
                        }
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

    // initialize all radiobuttons
    private void initRadio() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton sortById = (RadioButton) findViewById(R.id.sortById);
        // set "sortById" as default
        radioGroup.check(sortById.getId());
        radioGroup.setOnCheckedChangeListener(this);
    }

    // sort data by click radiobuttons
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.sortById:
                queryContentProvider(MyProviderContract._ID);
                radioFlag = "_id";
                break;
            case R.id.sortByDistance:
                queryContentProvider(MyProviderContract.DISTANCE);
                radioFlag = MyProviderContract.DISTANCE;
                break;
            case R.id.sortByPrice:
                queryContentProvider(MyProviderContract.PRICE);
                radioFlag = MyProviderContract.PRICE;
                break;
        }
    }
}