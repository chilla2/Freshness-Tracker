package com.example.freshnesstracker;

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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;

    private static final String TAG = "MainActivity";

    private FirebaseDatabase foodListDB;
    private DatabaseReference foodListDBReference;

    //variables for testing purposes
    Date mDate = new Date();
    FoodType mFoodType = FoodType.Dairy;

    //add in
    List<String> viewFoodList;

    //log cat tags
    public static final String TAG_CALL_DISPLAY = "CallDisplayFunc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<FoodItem> foodItemsList = new ArrayList<>();

        // will need this when list is ready: this.mainInventory = new ListManager(inventory);

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

        //Temporary work to get ListView to work with Array Adapter
        //ListManager fakeInventory = new ListManager(BuildFakeInventory.buildFakeInventory());

        //data base set up inventory
        ListManager dataBaseInventory = new ListManager(foodItemsList);

        //Adapter and place holder list
        viewFoodList = new ArrayList<>(); //question--activity main here?
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewFoodList);

        //connecting ArrayAdapter to ListView
        ListView listView = findViewById(R.id.recyclerView2);
        listView.setAdapter(arrayAdapter);

        //display fake inventory
        Log.d(TAG_CALL_DISPLAY, "About to call function in Create");
        //callDisplay(fakeInventory);

        //display database inventory
        callDisplay(dataBaseInventory);
    }

    //this will later be moved to the AddFoodItemActivity
    public void addFoodItem(Date date, String name, FoodType foodType) {
        FoodItem foodItem = new FoodItem(date, name, foodType);
        foodListDBReference.child("items").child(name).setValue(foodItem);
        //using setValue overwrites the data at the specified location.
        // to allow for multiple items with the same name, this will need to be changed
    }

    private void onSearch(View view){
        //this will be handled in its own activity
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

}