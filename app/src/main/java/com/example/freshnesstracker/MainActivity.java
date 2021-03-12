package com.example.freshnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;
    //RecyclerView Declarations
    private RecyclerView recyclerViewFoodItems;
    private RecyclerView.Adapter adapter;

    private static final String TAG = "MainActivity";

    private FirebaseDatabase foodListDB;
    private DatabaseReference foodListDBReference;

    //attempting to use Firebase view
    //FirebaseRecyclerOptions<FoodItem> options = new FirebaseRecyclerOptions.Builder<FoodItem>().setQuery(query, FoodItem.class).build();

    //variables for testing purposes
    Date mDate = new Date();
    FoodType mFoodType = FoodType.Dairy;

    //add in---temporary piece for list view
    List<String> viewFoodList;

    //log cat tags
    public static final String TAG_CALL_DISPLAY = "CallDisplayFunc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //START: Recycler View implementation with Fake Inventory List made in onCreate
        //TO DO: Put in onStart and get list from FIREBASE instead
        ArrayList<FoodItem> foodItemsList = new ArrayList<>();

        Log.d(TAG, "In on create start");
        this.mainInventory = new ListManager(BuildFakeInventory.buildFakeInventory());
        ArrayList<FoodItem> recyclerViewFoodItems = mainInventory.inventory;
        this.recyclerViewFoodItems = (RecyclerView) findViewById(R.id.recyclerView2);
        ListManager fakeInventory = new ListManager(BuildFakeInventory.buildFakeInventory());

        Log.d(TAG, "Now setting up adapter");

        adapter = new FoodItemAdapter(fakeInventory.inventory);

        this.recyclerViewFoodItems.setAdapter(adapter);
        Log.d(TAG, "Now calling layout manager");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.recyclerViewFoodItems.setLayoutManager(mLayoutManager);

        /*NOTE: To update list, use this code onStart (similar to what is present)
            foodItems.clear(); //your food item list used throughout, or get it from ListManager
            //work with adding items from adapter
            adapter.notifyDataSetChanged(); //just notify the data about the change.
            //Stack Overflow post with examples of working with updating RecyclerView Lists:
            //https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
         */

        //END Recycler View Active Code

        // Read from the database
        foodListDB = FirebaseDatabase.getInstance();
        foodListDBReference = foodListDB.getReference();

        //update array list as items are added to the database
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e("Get Data", postSnapshot.getValue(FoodItem.class).toString());
                    foodItemsList.add(postSnapshot.getValue(FoodItem.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        foodListDBReference.addValueEventListener(postListener);

      //testing
        addFoodItem(mDate, "Milk", mFoodType);

        //display fake inventory
        Log.d(TAG_CALL_DISPLAY, "About to call function in Create");
        //callDisplay(fakeInventory);

        //display database inventory
        //callDisplay(dataBaseInventory);
    }

    //this will later be moved to the AddFoodItemActivity
    public void addFoodItem(Date date, String name, FoodType foodType) {
        FoodItem foodItem = new FoodItem(date, name, foodType);
        foodListDBReference.child("items").child(name).setValue(foodItem);
        //using setValue overwrites the data at the specified location.
        // to allow for multiple items with the same name, this will need to be changed
    }

    public void onSearch(View view){
        //this will be handled in its own activity
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //called to display ListManager's to display; use on creation and for updates
    public void callDisplay(ListManager listManager) {


        //clear array adapter and current food view
        arrayAdapter.clear();
        viewFoodList.clear();

        //get required values from list; put filters here later?
        viewFoodList = listManager.makeDisplayStringArray();

        System.out.println(viewFoodList);
        Log.d(TAG_CALL_DISPLAY, "After view Food List Set");

        //get food list size
        int sizeList = viewFoodList.size();
        Log.d(TAG_CALL_DISPLAY, "The size of food list: " + sizeList); //correct
        //Log.d(TAG_CALL_DISPLAY, "The first item in food list: " + viewFoodList.get(0)); //correct

        //to avoid error, only print list if size list is non-zero
        if (sizeList > 0) {

            //add things to array adapter
            for (int i = 0; i < viewFoodList.size(); i++) {
                String lineToAdd = viewFoodList.get(i);
                arrayAdapter.add(lineToAdd);
                Log.d(TAG_CALL_DISPLAY, "Current index in displaying Loop: " + i);
            }
        }
        else {
            String zeroListErrorMessage = "No food items detected in list";
            arrayAdapter.add(zeroListErrorMessage);
        }

    }

    private void editItem() {
        //this will be handled in its own activity
    }
    public void onAdd(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }
    public void onSort(View view){
        //this will be handled in its own activity
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //will clear current reyclcer view of objects and re-display them; call after list updates
    public void resetRecyclerView() {
        //however data is passed:
        //data.clear();
        //adapter.notifyDataSetChanged();
    }

}