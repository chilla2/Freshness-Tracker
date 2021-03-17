package com.example.freshnesstracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FoodItemAdapter";

    private ArrayList<FoodItem> foodItems;
    public final ListItemClickListener mOnClickListener;

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    //static class for ViewHolder---used so it is in same namespace
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final View view;
        public final TextView name;
        public final TextView expirationDate;

        public TextView getName() {
            return name;
        }

        public TextView getExpirationDate() {
            return expirationDate;
        }

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            itemView.setOnClickListener(this);
            name = view.findViewById(R.id.name);
            expirationDate = view.findViewById(R.id.adapaterExpirDate);
            Log.d(TAG, "Constructor of ViewHolder called");
        }

        @Override
        public void onClick(View v) {
         int pos = getAdapterPosition();
         mOnClickListener.onListItemClick(pos);


        }
    }

    //constructor
    public FoodItemAdapter(ArrayList<FoodItem> foodItems, ListItemClickListener click) {
        this.foodItems = foodItems;
        this.mOnClickListener = click;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "In onCreateViewHolder");
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);

        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //link data to the list
        Log.d(TAG, "In onBindViewHolder");
        FoodItem foodItem = foodItems.get(position);
        //holder.getName().setText(foodItem.getName());
        ((ViewHolder) holder).getName().setText(foodItem.getName());
        ((ViewHolder) holder).getExpirationDate().setText(foodItem.getFormattedDate());

    }

    @Override
    public int getItemCount() {
        //get number of items in list to set list adapter

        if (foodItems != null) {
            Log.d(TAG, "Item count set: " + (foodItems.size()));
            return foodItems.size();
        }
        else{
            return 0;
        }
    }


}
