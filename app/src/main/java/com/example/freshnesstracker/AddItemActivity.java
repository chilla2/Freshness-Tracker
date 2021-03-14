package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";
    EditText editTextName;
    DatePicker picker;
    Spinner spinnerCategory;
    Button saveItem;
    DatabaseReference databaseItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        databaseItems = FirebaseDatabase.getInstance().getReference("items");
        editTextName = (EditText) findViewById(R.id.editTextName);
        picker = (DatePicker)findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.categories_spinner);
        saveItem = (Button) findViewById(R.id.saveItem);
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                switchToMain();
            }
        });
    }

    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }

    /*
     * This method is saving a new item to the
     * Firebase Realtime Database
     * */
    private void addItem() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();
        String category = spinnerCategory.getSelectedItem().toString();
        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our item
            String id = databaseItems.push().getKey();
            //creating an item Object
            FoodItem foodItem = new FoodItem(id, day, month, year, name, category);
            //Saving the item
            databaseItems.child(id).setValue(foodItem);
            Log.d(TAG, "Adding item data to database");
            //setting edittext to blank again
            editTextName.setText("");
            //displaying a success toast
            Toast.makeText(this, "Item added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}