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

/**
 * Customized RecycleView.Adapter for generating and displaying FoodItem class information in RecyclerView object
 */
public class FoodItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FoodItemAdapter";

    private ArrayList<FoodItem> foodItems;
    public final ListItemClickListener mOnClickListener;

    //set listner for items
    interface ListItemClickListener{
        void onListItemClick(int position);
    }


    /**
     * Static class that customizes the viewHolder for FoodItemAdapter
     */
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

        /**
         *Constructor to set the display/view of the FoodItem information
         * @param view
         */
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

    /**
     * Constructor for FoodItemAdapter
     * @param foodItems
     * @param click
     */
    public FoodItemAdapter(ArrayList<FoodItem> foodItems, ListItemClickListener click) {
        this.foodItems = foodItems;
        this.mOnClickListener = click;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "In onCreateViewHolder");
        //attach view holder to item_food XML layout
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //link data to the list for display
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

        //set text color based on whether item has expired or not
        if (foodItem.getIsExpired()) {
            ((ViewHolder) holder).getName().setTextColor(Color.parseColor("#5c1a1b"));
        } else {
            ((ViewHolder) holder).getName().setTextColor(Color.parseColor("#386150"));
        }
    }

    @Override
    public int getItemCount() {
        //get number of UNIQUE items in list to set list adapter
        Log.d(TAG, "Unique item count is: " + (foodItems.size()));
        return foodItems.size();
    }
}