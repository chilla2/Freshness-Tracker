package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditItemActivity extends AppCompatActivity {
    private static final String TAG = "EditItemActivity";
    EditText editTextName;
    DatePicker picker;
    Spinner spinnerCategory;
    Button saveItem;
    DatabaseReference databaseItem;
    String updateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);//get the current intent
        Intent intent = getIntent();

        //get the attached extras from the intent
        updateId = intent.getStringExtra("itemId");
        String name = intent.getStringExtra("name");
        int day = intent.getIntExtra("day", 1);
        int month = intent.getIntExtra("month", 1);
        int year = intent.getIntExtra("year", 2022);
        String category = intent.getStringExtra("category");


        //get reference for specific item using the ID
        databaseItem = FirebaseDatabase.getInstance().getReference("foodItemsList").child(updateId);
        //DataSnapshot postSnapshot = dataSnapshot.getChildren();
        //FoodItem foodItem = databaseItem.getValue(FoodItem.class);
        editTextName = (EditText) findViewById(R.id.foodName);
        picker = (DatePicker) findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.foodType);
        saveItem = (Button) findViewById(R.id.saveItem);

        //set default values to be the previous values of the item
        editTextName.setText(name);
        //Figure out what position the category is assigned to and set the spinner position to that one
        ArrayAdapter<String> spinnerCategoryAdapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
        int spinnerPosition = spinnerCategoryAdapter.getPosition(category);
        spinnerCategory.setSelection(spinnerPosition);

        picker.init(year, month, day, null);


        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get values from user, whether they were left the same or not
                String name = editTextName.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                int day = picker.getDayOfMonth();
                int month = picker.getMonth();
                int year = picker.getYear();
                FoodItem food = new FoodItem(updateId, year, month, day, name, category);
                databaseItem.setValue(food);

                //FoodType foodType = assignFoodType(category);

                //Can't access itemId or intent from here, so must use getIntent and getExtra again in order to retrieve itemId to be passed into updateItem
                Intent intent = getIntent();
                String itemId = intent.getStringExtra("itemId");

                if (!TextUtils.isEmpty(name)) {
                    updateItem(itemId, day, month, year, name, category);
                    switchToMain();
                }
            }
        });


    }

    //go back to main, where onDataChange will be called upon returning
    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }

    //This is where the database is actually changed
    private boolean updateItem(String id, int day, int month, int year, String name, String category) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("foodItems").child(id);
        //updating item
        FoodItem foodItem = new FoodItem(id, day, month, year, name, category);
        dR.setValue(foodItem);
        Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_LONG).show();
        return true;
    }
}