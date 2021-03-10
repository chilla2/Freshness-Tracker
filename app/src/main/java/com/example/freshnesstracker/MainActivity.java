package com.example.freshnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.DatePicker;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    DatePicker picker;
    Spinner spinnerCategory;
    ListView listViewItems;

    Button buttonAddItem;

    //a list to store all the artist from firebase database
    List<FoodItem> foodItems;

    //our database reference object
    DatabaseReference databaseItems;

    private static final String TAG = "MainActivity";

    private FirebaseDatabase foodListDB;
    private DatabaseReference foodListDBReference;
    ArrayList<FoodItem> foodItemsList;

    //variables for testing purposes
    Date mDate = new Date();
    FoodType mFoodType = FoodType.Dairy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of artists node
        databaseItems = FirebaseDatabase.getInstance().getReference("items");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        picker=(DatePicker)findViewById(R.id.datePicker);
        spinnerCategory = (Spinner) findViewById(R.id.categories_spinner);
        listViewItems = (ListView) findViewById(R.id.listViewItems);

        buttonAddItem = (Button) findViewById(R.id.buttonAddItem);

        //list to store food items
        foodItems = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addItem()
                //the method is defined below
                //this method is actually performing the write operation
                addItem();
            }
        });

        //attaching listener to listview
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected item
                 FoodItem foodItem = foodItems.get(i);
            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodItem foodItem = foodItems.get(i);
                showUpdateDeleteDialog(foodItem.getItemId(), foodItem.getName());
                return true;
            }
        });
    }


    private void showUpdateDeleteDialog(final String itemId, String itemName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialogue, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.categories_spinner);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);

        dialogBuilder.setTitle(itemName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                int day = picker.getDayOfMonth();
                int month = picker.getMonth();
                int year = picker.getYear();
                if (!TextUtils.isEmpty(name)) {
                    updateItem(itemId, day, month, year, name, category);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItem(itemId);
                b.dismiss();
            }
        });
    }

    private boolean updateItem(String id, int day, int month, int year, String name, String category) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(id);

        //updating item
        FoodItem foodItem = new FoodItem(id, day, month, year, name, category);
        dR.setValue(foodItem);
        Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteItem(String id) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(id);

        //removing item
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous items list
                foodItems.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting item
                    FoodItem foodItem = postSnapshot.getValue(FoodItem.class);
                    //adding item to the list
                    foodItems.add(foodItem);
                }

                //creating adapter
                FoodItemsList artistAdapter = new FoodItemsList(MainActivity.this, foodItems);
                //attaching adapter to the listview
                listViewItems.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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