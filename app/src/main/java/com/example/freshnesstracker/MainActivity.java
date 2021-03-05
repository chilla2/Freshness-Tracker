package com.example.freshnesstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;

    private static final String TAG = "MainActivity";

    private FirebaseDatabase foodListDB;
    private DatabaseReference foodListDBReference;

    //variables for testing purposes
    Date mDate = new Date();
    FoodType mFoodType = FoodType.Dairy;

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

        addFoodItem(mDate, "Milk", mFoodType);
    }

    //this will later be moved to the AddFoodItemActivity
    public void addFoodItem(Date date, String name, FoodType foodType) {
        FoodItem foodItem = new FoodItem(date, name, foodType);
        foodListDBReference.child("items").child(name).setValue(foodItem);
        //using setValue overwrites the data at the specified location.
        // to allow for multiple items with the same name, this will need to be changed
    }
}