package com.example.tracksystem.tracksystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private ArrayList<table_Items> table_items;
    Context context;

    public myAdapter(ArrayList<table_Items> table_items, Context context) {
        this.table_items = table_items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_design, null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.trainID.setText(table_items.get(position).trainID);
        holder.train_class.setText(table_items.get(position).trainClass);
        holder.arrival_time.setText(table_items.get(position).arrival_time);
        holder.dep_time.setText(table_items.get(position).dep_time);
    }

    @Override
    public int getItemCount() {
        return table_items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trainID, train_class, arrival_time, dep_time;


        public ViewHolder(View itemView) {
            super(itemView);

            trainID = (TextView) itemView.findViewById(R.id.table_trainID);
            train_class = (TextView) itemView.findViewById(R.id.table_train_class);
            arrival_time = (TextView) itemView.findViewById(R.id.table_arrival_time);
            dep_time = (TextView) itemView.findViewById(R.id.table_trainTime);


        }

    }
}
