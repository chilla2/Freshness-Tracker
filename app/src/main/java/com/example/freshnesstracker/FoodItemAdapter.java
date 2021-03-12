package com.example.freshnesstracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FoodItemAdapter";

    private ArrayList<FoodItem> foodItems;

    //static class for ViewHolder---used so it is in same namespace
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
            name = view.findViewById(R.id.name);
            expirationDate = view.findViewById(R.id.adapaterExpirDate);
            Log.d(TAG, "Constructor of ViewHolder called");
        }
    }

    //constructor
    public FoodItemAdapter(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
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
            return foodItems.size();
        }
        else{
            return 0;
        }
    }


}
