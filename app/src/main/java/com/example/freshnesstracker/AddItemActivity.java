package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.core.view.View;

import java.util.Date;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddItemActivity extends AppCompatActivity {

    private FirebaseDatabase foodListDB;
    private DatabaseReference databaseItems;;
    private static final String TAG = "AddItemActivity";
    EditText editTextName;
    DatePicker picker;
    Spinner spinnerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        databaseItems = FirebaseDatabase.getInstance().getReference("foodItemsList");

        //getting views
        editTextName = (EditText) findViewById(R.id.foodName);
        picker = (DatePicker) findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.foodType);

        /*Spinner spinner = (Spinner) findViewById(R.id.foodType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.foodTypeList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
*/

        //foodListDB = FirebaseDatabase.getInstance();


    }

    public void onDone(View view){
        String name = editTextName.getText().toString().trim();
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();
        year -= 1900;
        String category = spinnerCategory.getSelectedItem().toString();
        //FoodType foodType = assignFoodType(category);
        String id = databaseItems.push().getKey();
        FoodItem food = new FoodItem(id, year, month, day, name, category);
        databaseItems.child(id).setValue(food);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

   /* public FoodType assignFoodType(String string){
        FoodType t;
       switch(string){
           
           case ("Dairy"):
               t = FoodType.Dairy;
               break;
           case ("Produce"):
               t = FoodType.Produce;
               break;
           case ("Bakery"):
               t = FoodType.Bakery;
               break;
           case ("Meat"):
               t = FoodType.Meat;
               break;
           case ("Custom"):
               t = FoodType.Custom;
               break;
           case ("Canned"):
               t = FoodType.Dry;
               break;

           default:
               throw new IllegalStateException("Unexpected value: " + string);
       }
           return t;

       
    }*/
}