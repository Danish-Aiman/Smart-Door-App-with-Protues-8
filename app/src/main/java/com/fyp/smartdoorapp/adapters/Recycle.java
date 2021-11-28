package com.fyp.smartdoorapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.smartdoorapp.R;

import java.util.ArrayList;

public class Recycle  extends RecyclerView.Adapter {
    ArrayList<String> histlist;
    Context context;

    public Recycle(Context context, ArrayList<String> histlist) {
        this.histlist = histlist;
        this.context = context;
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textcolor_pages, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder myholder = (ViewHolder) holder;
        myholder.text_item2.setText(histlist.get(position));
    }

    @Override public int getItemCount() {

        return histlist.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        TextView text_item2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //text_item2 = itemView.findViewById(R.id.text_item2);
        }
    }
}
