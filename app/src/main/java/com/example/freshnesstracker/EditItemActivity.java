package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.NumberPicker;

public class EditItemActivity extends AppCompatActivity {

    private static final String TAG = "EditItemActivity";
    EditText editTextName;
    DatePicker picker;
    Spinner spinnerCategory;
    NumberPicker quantityPicker;
    Button saveItem;
    Button cancel;
    String PATH;
    DatabaseReference databaseItem;

    /**
     *  onCreate retrieves the data associated with the selected item and sets the defaults for the input fields accordingly.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //get the current intent
        Intent intent = getIntent();

        //get the attached extras from the intent
        String itemId  = intent.getStringExtra("itemId");
        String name = intent.getStringExtra("name");
        int day = intent.getIntExtra("day", 1);
        int month = (intent.getIntExtra("month", 1)) - 1;
        int year = intent.getIntExtra("year", 2022);
        String category = intent.getStringExtra("category");
        int quantity = intent.getIntExtra("quantity", 1);

        //set autofill
        setAutofill();

        //get unique database reference key from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PATH = sharedPreferences.getString("key", null);

        //get reference for specific item using the ID
        databaseItem = FirebaseDatabase.getInstance().getReference(PATH).child(itemId);

        editTextName = (EditText) findViewById(R.id.editTextName);
        picker = (DatePicker)findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.categories_spinner);
        quantityPicker = (NumberPicker) findViewById(R.id.quantityPicker);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(20);
        saveItem  = (Button) findViewById(R.id.saveItem);
        cancel  = (Button) findViewById(R.id.cancel);

        //set default values to be the previous values of the item
        editTextName.setText(name);
        picker.init(year, month, day, null);
        quantityPicker.setValue(quantity);
        //Figure out what position the category is assigned to and set the spinner position to that one
        ArrayAdapter<String> spinnerCategoryAdapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
        int spinnerPosition = spinnerCategoryAdapter.getPosition(category);
        spinnerCategory.setSelection(spinnerPosition);

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get values from user, whether they were left the same or not
                String name = editTextName.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                int day = picker.getDayOfMonth();
                int month = picker.getMonth() + 1;
                int year = picker.getYear();
                int quantity = quantityPicker.getValue();

                //Can't access itemId or intent from here, so must use getIntent and getExtra again in order to retrieve itemId to be passed into updateItem
                Intent intent = getIntent();
                String itemId= intent.getStringExtra("itemId");

                if (!TextUtils.isEmpty(name)) {
                    updateItem(itemId, day, month, year, name, category, quantity);
                    switchToMain();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchToMain();
            }
        });

    }

    /**
     *  switchToMain returns to the Main Actvity.
     */
    //go back to main, where onDataChange will be called upon returning
    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }

    /**
     *  updateItem locates the selected item in the database and updates its properties.
     */
    //This is where the database is actually changed
    private boolean updateItem(String id, int day, int month, int year, String name, String category, int quantity) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(PATH).child(id);
        //updating item
        FoodItem foodItem = new FoodItem(id, day, month, year, name, category, quantity);
        dR.setValue(foodItem);
        Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    /***
     * setAutoFill prepares the autofilltextview display element by loading the food list
     * suggestions from the "foodItemSuggestions.xml" and setting it.
     */
    private void setAutofill() {
        //get autofill ready, extract resources from file---MUST be called in onCreate
        Resources res = getResources();
        String[] FOOD_ITEM_SUGGESTIONS = res.getStringArray(R.array.Food_Item_Suggestions);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this,
                android.R.layout.simple_dropdown_item_1line, FOOD_ITEM_SUGGESTIONS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editTextName);
        textView.setAdapter(arrayAdapter);
    }
}