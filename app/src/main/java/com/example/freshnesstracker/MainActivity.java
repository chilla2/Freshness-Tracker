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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.DatePicker;

public class MainActivity extends AppCompatActivity {

    DatePicker picker;
    ListView listViewItems;
    //a list to store all the artist from firebase database
    List<FoodItem> foodItems;
    FloatingActionButton addButton;
    //our database reference object
    DatabaseReference databaseItems;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Calling onCreate method");
        setContentView(R.layout.activity_main);
        //getting the reference of items node
        databaseItems = FirebaseDatabase.getInstance().getReference("items");
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        listViewItems = (ListView) findViewById(R.id.listViewItems);
        //list to store food items
        foodItems = new ArrayList<>();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Log.d(TAG, "Switching to add item activity");
                switchToAddItem();
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
    private void switchToAddItem() {
        Intent switchToAddItemIntent = new Intent(this, AddItemActivity.class);
        startActivity(switchToAddItemIntent);
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
        Log.d(TAG, "Calling onStart method");
        //attaching value event listener
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Calling onDataChange method");
                //Call function to iterate through DB nodes and add items to list
                List<FoodItem> workingList = loopThroughDBAndAddToList(dataSnapshot);
                if (!(workingList.size() == 0)) {
                    Log.d(TAG, "Working list is not empty");
                } else {
                    Log.d(TAG, "Working list is empty");
                }
                //creating adapter
                FoodItemsList itemAdapter = new FoodItemsList(MainActivity.this, workingList);
                Log.d(TAG, "Attaching adapter to listViewItems");
                    //attaching adapter to the listview
                    listViewItems.setAdapter(itemAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //This method is called in the onDataChange method (in onStart)
    public List<FoodItem> loopThroughDBAndAddToList(DataSnapshot dataSnapshot) {
        Log.d(TAG, "Running method - loopThroughDBAndAddToList");
        Log.d(TAG, "Clearing list");
        foodItems.clear();
        //iterating through all the nodes
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Log.d(TAG, "Getting item...");
            //getting item
            FoodItem foodItem = postSnapshot.getValue(FoodItem.class);
            //adding item to the list
            Log.d(TAG, "Adding item to list");
            foodItems.add(foodItem);
        }
        return foodItems;
    }
}