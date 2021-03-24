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
import java.util.Date;
import java.util.Calendar;

/**
 * Customized RecycleView.Adapter for generating and displaying FoodItem class information in RecyclerView object
 */
public class FoodItemAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FoodItemAdapter";

    private ArrayList<FoodItem> foodItems;
    public final ListItemClickListener mOnClickListener;

    //color date change variables
    Date currentTime = Calendar.getInstance().getTime();
    //code credit: https://stackoverflow.com/questions/4902653/java-util-date-seven-days-ago
    long DAY_IN_MS = 1000 * 60 * 60 * 24;
    Date sevenDaysAheadDate= new Date(System.currentTimeMillis() + (7 * DAY_IN_MS));

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    //static class for ViewHolder---used so it is in same namespace

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
     *
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

        Log.d(TAG, "About to change colors");
        //set customized color change for dates
        int colorCase = getExpirationDateCase(foodItem.getDate());

        switch(colorCase) {
            case 0: ((ViewHolder) holder).name.setTextColor(Color.GREEN);
                break;
            case 1: ((ViewHolder) holder).name.setTextColor(Color.YELLOW);
                break;
            case 2: ((ViewHolder) holder).name.setTextColor(Color.RED);
                break;

        }



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

    private int getExpirationDateCase(Date expirationDate) {

        int resultingCase = 0; //default case---green

        //case: food expires within 7 days--yellow
        if ((expirationDate.getTime() > currentTime.getTime()) && (expirationDate.getTime() < sevenDaysAheadDate.getTime())) {
            resultingCase = 1;
            Log.d(TAG, "Resulting case 1 active in getExpirFunc");
        }

        //case: expiration date has passed, lower value than current date
        if (expirationDate.getTime() < currentTime.getTime()) {
            resultingCase = 2;
            Log.d(TAG, "Resulting case 2 active in getExpirFunc");
        }

        Log.d(TAG, "Final case report: " + resultingCase);
        return resultingCase;
    }


}
