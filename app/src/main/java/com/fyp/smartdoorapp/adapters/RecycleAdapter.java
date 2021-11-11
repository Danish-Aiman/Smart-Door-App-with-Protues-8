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

public class RecycleAdapter extends RecyclerView.Adapter {
    ArrayList<String> history;
    Context context;

    public RecycleAdapter(Context context, ArrayList<String> history) {
        this.history = history;
        this.context = context;
    }

    public void  updatedata (ArrayList<String>update){
        history.add(String.valueOf(update));
        notifyDataSetChanged();
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textcolor_pages, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder myholder = (ViewHolder) holder;
        myholder.text_item.setText(history.get(position));
    }

    @Override public int getItemCount() {
        return history.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        TextView text_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_item = itemView.findViewById(R.id.text_item);
        }
    }
}
