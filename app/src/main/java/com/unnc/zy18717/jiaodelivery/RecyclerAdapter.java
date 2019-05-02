package com.unnc.zy18717.jiaodelivery;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {

    Context context;
    Cursor cursor;

    public RecyclerAdapter (Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public void update (Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;

        public MyHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.value1);
            textView2 = itemView.findViewById(R.id.value2);
            textView3 = itemView.findViewById(R.id.value3);
        }
    }

    @Override
    public MyHolder onCreateViewHolder (ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder (MyHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            holder.textView1.setText(String.valueOf(id));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            holder.textView2.setText("$" + price);
            String distance = cursor.getString(cursor.getColumnIndex("distance"));
            holder.textView3.setText(distance + "km");
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }
}