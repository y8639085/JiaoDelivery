package com.unnc.zy18717.jiaodelivery;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.MyHolder> implements View.OnClickListener{

    Context context;
    Cursor cursor;

    public RecyclerAdapter2 (Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public void update (Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6;
        Button setPrice, setStatus;

        public MyHolder(final View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.value1);
            textView2 = itemView.findViewById(R.id.value2);
            textView3 = itemView.findViewById(R.id.value3);
            textView4 = itemView.findViewById(R.id.value4);
            textView5 = itemView.findViewById(R.id.value5);
            textView6 = itemView.findViewById(R.id.value6);
            setPrice = (Button)itemView.findViewById(R.id.setPrice);
            setStatus = (Button)itemView.findViewById(R.id.setStatus);

            itemView.setOnClickListener(RecyclerAdapter2.this);
            setPrice.setOnClickListener(RecyclerAdapter2.this);
            setStatus.setOnClickListener(RecyclerAdapter2.this);
        }
    }

    @Override
    public RecyclerAdapter2.MyHolder onCreateViewHolder (ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_item_layout, parent, false);
        return new RecyclerAdapter2.MyHolder(view);
    }

    @Override
    public void onBindViewHolder (RecyclerAdapter2.MyHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            holder.textView1.setText(String.valueOf(id));
            String pickUp = cursor.getString(cursor.getColumnIndex("pickUp"));
            holder.textView2.setText(pickUp);
            String des = cursor.getString(cursor.getColumnIndex("des"));
            holder.textView3.setText(des);
            String distance = cursor.getString(cursor.getColumnIndex("distance"));
            holder.textView4.setText(distance + "km");
            String price = cursor.getString(cursor.getColumnIndex("price"));
            holder.textView5.setText("$" + price);
            String status = cursor.getString(cursor.getColumnIndex("status"));
            holder.textView6.setText(status);

            holder.itemView.setTag(position);
            holder.setPrice.setTag(position);
            holder.setStatus.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        int position = (int)view.getTag(); //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (view.getId()) {
                case R.id.recyclerView:
                    System.out.println("recyclerView");
                    mOnItemClickListener.onItemClick(view, position);
                    break;
                default:
                    System.out.println("default");
                    mOnItemClickListener.onItemClick(view, position);
                    break;
            }
        }
    }
}