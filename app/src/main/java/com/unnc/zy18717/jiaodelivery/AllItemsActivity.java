package com.unnc.zy18717.jiaodelivery;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;

public class AllItemsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allitems);

        queryContentProvider(null);
        initRadio();
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
                queryContentProvider("_id");
                break;
            case R.id.sortByDistance:
                queryContentProvider("ABS(`"+MyProviderContract.DISTANCE+"`)");
                break;
            case R.id.sortByPrice:
                queryContentProvider("price");
                break;
        }
    }
}