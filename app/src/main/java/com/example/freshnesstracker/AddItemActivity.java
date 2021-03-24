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

import java.util.Calendar;
import java.util.Date;
import android.widget.NumberPicker;


public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";
    EditText editTextName;
    NumberPicker quantityPicker;
    DatePicker datePicker;
    Spinner spinnerCategory;
    Button saveItem;
    Button cancel;
    DatabaseReference databaseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        databaseItems = FirebaseDatabase.getInstance().getReference("items");
        editTextName = (EditText) findViewById(R.id.editTextName);
        quantityPicker = (NumberPicker) findViewById(R.id.quantityPicker);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(20);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.categories_spinner);
        saveItem = (Button) findViewById(R.id.saveItem);
        cancel  = (Button) findViewById(R.id.cancel);
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                switchToMain();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchToMain();
            }
        });
    }

    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }

    private void addItem() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        int quantity = quantityPicker.getValue();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String category = spinnerCategory.getSelectedItem().toString();
        //checking if name is provided
        if (!TextUtils.isEmpty(name)) {
            //getting a unique id using push().getKey() method
            String id = databaseItems.push().getKey();
            //creating an item Object
            FoodItem foodItem = new FoodItem(id, day, month, year, name, category, quantity);
            //Saving the item
            databaseItems.child(id).setValue(foodItem);
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