package com.example.freshnesstracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static androidx.recyclerview.widget.RecyclerView.*;

/**
 * Main Activity displays list of food items and their expiration dates from realtime database -
 * Add button in button in bottom right corner switches view to add item screen -
 * Long press on item brings up dialog with buttons to add or delete item -
 */

public class MainActivity extends AppCompatActivity implements FoodItemAdapter.ListItemClickListener {

    private Spinner mSpinner;
    FloatingActionButton addButton;

    //creating one list to contain all items and remains unchanged except when DB is updated, and one list that will be updated depending on needs of view.
    ArrayList<FoodItem> foodItems;
    ArrayList<FoodItem> displayList;

    RecyclerView recyclerViewFoodItems;
    FoodItemAdapter adapter;

    DatabaseReference databaseItems;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Calling onCreate method");
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        mSpinner = findViewById(R.id.foodType);

        databaseItems = FirebaseDatabase.getInstance().getReference("items");

        foodItems = new ArrayList<>(); //list to store all food items (updated when database changes)
        displayList = new ArrayList<>(); //list to store items in one category only (gets updated in switchToType())

        //will this work if foodItems is empty?
        adapter = new FoodItemAdapter(displayList, this);

        recyclerViewFoodItems = findViewById(R.id.recyclerView2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewFoodItems.setLayoutManager(mLayoutManager);
        recyclerViewFoodItems.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
                Log.d(TAG, "Switching to add item activity");
                switchToAddItem();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Calling onStart method");
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Calling onDataChange method");
                foodItems.clear();
                displayList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e("Get Data", postSnapshot.getValue(FoodItem.class).toString());
                    foodItems.add(postSnapshot.getValue(FoodItem.class));
                    displayList.add(postSnapshot.getValue(FoodItem.class));
                }
                if (!(foodItems.size() == 0)) {
                    sortByExpiry(foodItems);
                    sortByExpiry(displayList);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //when a selection is made in the foodType dropdown
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sortSelection = mSpinner.getSelectedItem().toString();
                displayByType(sortSelection);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void onListItemClick(int position) {
        FoodItem foodItem = displayList.get(position);
        Log.d("Click", foodItem.getName());
        showUpdateDeleteDialog(foodItem);
    }

    /** showUpdateDeleteDialog is called on long press of an item in the list. The dialog provides the update and delete buttons. */
    //This function provides the update and delete buttons
    private void showUpdateDeleteDialog(FoodItem foodItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialogue, null);
        dialogBuilder.setView(dialogView);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);
        dialogBuilder.setTitle(foodItem.getName());
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToEditItem(foodItem);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(foodItem.getItemId());
                dialog.dismiss();
            }
        });
    }

    /** switchToAddItem method changes activities to the Add Item. This is called when the add button is clicked */
    private void switchToAddItem() {
        Intent switchToAddItemIntent = new Intent(this, AddItemActivity.class);
        startActivity(switchToAddItemIntent);
    }

    private void switchToEditItem(FoodItem foodItem) {
        //creating an intent
        Intent switchToEditItemIntent = new Intent(this, EditItemActivity.class);
        //adding item data intent
        switchToEditItemIntent.putExtra("itemId", foodItem.getItemId());
        switchToEditItemIntent.putExtra("name", foodItem.getName());
        switchToEditItemIntent.putExtra("day", foodItem.getDay());
        switchToEditItemIntent.putExtra("month", foodItem.getMonth());
        switchToEditItemIntent.putExtra("year", foodItem.getYear());
        switchToEditItemIntent.putExtra("category", foodItem.getFoodType());
        //starting the edit activity with intent
        Log.d(TAG, "Switching to Edit Item Activity");
        startActivity(switchToEditItemIntent);
    }

    private boolean deleteItem(String id) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(id);
        //removing item
        dR.removeValue();
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    public void sortByExpiry(ArrayList<FoodItem> foodItems){
        if (foodItems.size() != 0) {
            Comparator<FoodItem> dateComparator = (o1, o2) -> {
                Date date1 = new Date(o1.year, o1.month, o1.day);
                Date date2 = new Date(o2.year, o2.month, o2.day);
                return date1.compareTo(date2);
            };
            Collections.sort(foodItems, dateComparator);
        }
    }

    /* This method is called upon selection of food type dropdown.
    It adds the items that match the food type chosen to a list, replaces the old adapter, and attaches the adapter to the list.
    This method does not change the list that contains all food items.
    Nothing happens if the all food items list is empty.
    The adapter/visible list doesn't change if the new list is empty.
     */
    private void displayByType(String sortSelection) {
        displayList.clear();
        Log.d(TAG, "Sort Selection:" + sortSelection);
        if (foodItems.size() != 0) {
            //if selection is All, add all items to display list and update adapter
            if (sortSelection.equals("All")) {
                for (FoodItem foodItem : foodItems) {
                    displayList.add(foodItem);
                }
                Log.d(TAG, "Added all items to list");
            } else {
                for (FoodItem foodItem : foodItems) {
                    if (foodItem.getFoodType().equals(sortSelection)) {
                        Log.d(TAG, "Adding item to display list");
                        displayList.add(foodItem);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}