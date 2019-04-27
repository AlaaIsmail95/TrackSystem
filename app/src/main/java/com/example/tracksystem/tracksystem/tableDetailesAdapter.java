package com.example.tracksystem.tracksystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class tableDetailesAdapter extends RecyclerView.Adapter<tableDetailesAdapter.ViewHolder> {
    private ArrayList<tableDetailsItems> tabledetailsItems;
    Context context;

    public tableDetailesAdapter(ArrayList<tableDetailsItems> tabledetailsItems, Context context) {
        this.tabledetailsItems = tabledetailsItems;
        this.context = context;
    }

    @Override
    public tableDetailesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_detailes_design, null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(tableDetailesAdapter.ViewHolder holder, int position) {

        holder.arr_staion.setText(tabledetailsItems.get(position).station);
        holder.arr_time.setText(tabledetailsItems.get(position).arrival_time);

    }

    @Override
    public int getItemCount() {
        return tabledetailsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView arr_staion, arr_time;

        public ViewHolder(View itemView) {
            super(itemView);

            arr_staion = (TextView) itemView.findViewById(R.id.tableD_train_st);
            arr_time = (TextView) itemView.findViewById(R.id.tableD_train_time);
        }
    }
}
