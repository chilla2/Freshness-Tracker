package com.example.freshnesstracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class FoodItemAdapter extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "FoodItemAdapter";

    private ArrayList<FoodItem> foodItems;
    protected ArrayList<FoodItem> foodItemsCopy;

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
        this.foodItemsCopy.addAll(foodItems);
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

//    // Filter Class
//    public void filter(String query) {
//        query = query.toLowerCase(Locale.getDefault());
//        foodItems.clear();
//        if (query.length() == 0) {
//            foodItems.addAll(foodItemsCopy);
//        } else {
//            for (FoodItem foodItemFilter : foodItemsCopy) {
//                if (foodItemFilter.getName().toLowerCase(Locale.getDefault()).contains(query)) {
//                    foodItems.add(foodItemFilter);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public Filter getFilter()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<FoodItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(foodItemsCopy);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FoodItem item : foodItemsCopy) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            foodItems.clear();
            foodItems.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
