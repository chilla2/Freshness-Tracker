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

/**
 * AddItem Activity takes inputs from the user to create a foodItem object.
 * Inputs for name of food item, category of food item, quantity of fooditem,
 * and an use by spinner. Add button creates food item and pushes it to the database
 * and returns the user to the main page. Cancel button returns the user to the main
 * page without saving the data.
 */
public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";
    EditText editTextName;
    NumberPicker quantityPicker;
    DatePicker datePicker;
    Spinner spinnerCategory;
    Button saveItem;
    Button cancel;
    String PATH;
    DatabaseReference databaseItems;

    /**
 *  onCreate assigns variables and the database reference. It also sets the click listeners
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PATH = sharedPreferences.getString("key", null);
        databaseItems = FirebaseDatabase.getInstance().getReference(PATH);
        editTextName = (EditText) findViewById(R.id.editTextName);
        quantityPicker = (NumberPicker) findViewById(R.id.quantityPicker);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(20);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.categories_spinner);
        saveItem = (Button) findViewById(R.id.saveItem);/**/
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

        //set auto-fill
        setAutofill();

    }
/**
 * switchToMain returns the user to them MainActivity
 * */
    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }
/**
 * addItem gets the user input, gets a new id and creates a food item.
 * It adds the new object to the database and displays a toast informing the
 * user that the item was successfully added.  If the method is called before
 * the user enters a name for the fooditem at toast prompts the user to enter a name.
 * */
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