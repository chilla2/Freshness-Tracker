package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddItemActivity extends AppCompatActivity {

    private FirebaseDatabase foodListDB;
    private DatabaseReference foodListDBReference;
    private static final String TAG = "AddItemActivity";


public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Spinner spinner = (Spinner) findViewById(R.id.foodType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.foodTypeList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        foodListDB = FirebaseDatabase.getInstance();
        foodListDBReference = foodListDB.getReference();


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new item has been added, add it to the displayed list
                FoodItem foodItem = dataSnapshot.getValue(FoodItem.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // An item has changed, use the key to determine if we are displaying this
                // item and if so displayed the changed item.
                FoodItem newItem = dataSnapshot.getValue(FoodItem.class);
                String itemKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // An item has changed, use the key to determine if we are displaying this
                // item and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                // FoodItem movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
        foodListDBReference.addChildEventListener(childEventListener);


    }

    /*
    private FoodItem getNewItemData() {
        This should take input from user and create food item to be passed into the save method
    }
    */




    //Later, this method can just take a FoodItem as a parameter
    public void saveItem(Date date, String name, FoodType foodType) {

    }

    public void onRadioButtonClicked(View view) {
    }
}