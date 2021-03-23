package com.example.freshnesstracker;

import android.graphics.Color;
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

        //duplicate items (quantity > 1) will display their quantity next to their name
        //single items display name only
        String nameAndQuantity = foodItem.getName() + " (" + foodItem.getQuantity()  + ")";
        if (foodItem.getQuantity() > 1) {
            ((ViewHolder) holder).getName().setText(nameAndQuantity);
        } else {
            ((ViewHolder) holder).getName().setText(foodItem.getName());
        }
        String formattedDate = foodItem.month + "/" + foodItem.day + "/" + foodItem.year;
        ((ViewHolder) holder).getExpirationDate().setText(formattedDate);
        ((ViewHolder) holder).getExpirationDate().setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        //get number of UNIQUE items in list to set list adapter
        Log.d(TAG, "Unique item count is: " + (foodItems.size()));
        return foodItems.size();
    }

}