package com.example.freshnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FoodItemAdapter.ListItemClickListener {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;

    EditText editTextName;
    ListView listViewItems;
    //Button buttonAddItem;

    //our database reference object
    DatabaseReference databaseItems;
    ArrayList<FoodItem> foodItems;

    //RecyclerView Declarations
    RecyclerView recyclerViewFoodItems;
    RecyclerView.Adapter adapter;

    private static final String TAG = "MainActivity";

    private FirebaseDatabase foodListDB;


    //attempting to use Firebase view
    //FirebaseRecyclerOptions<FoodItem> options = new FirebaseRecyclerOptions.Builder<FoodItem>().setQuery(query, FoodItem.class).build();

    //variables for testing purposes
    //Date mDate = new Date();
    //FoodType mFoodType = FoodType.Dairy;

    //add in---temporary piece for list view


    //log cat tags
    public static final String TAG_CALL_DISPLAY = "CallDisplayFunc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of FoodItem node
        databaseItems = FirebaseDatabase.getInstance().getReference("foodItemsList");

        //START: Recycler View implementation with Fake Inventory List made in onCreate
        //TO DO: Put in onStart and get list from FIREBASE instead

        // Changed this name to foodItems to merge different codes and initiated the property with fake inventory
        // **ArrayList<FoodItem> foodItemsList = new ArrayList<>();
        foodItems = BuildFakeInventory.buildFakeInventory();
        Log.d(TAG, "In on create start");
        // initiated with the variable instead of hardcode
        // **this.mainInventory = new ListManager(BuildFakeInventory.buildFakeInventory());
        this.mainInventory = new ListManager(foodItems);

        // This code is using the same name as the RecyclerView, can it be renamed? Do we need it?  I don't see where it is being used.
        // **ArrayList<FoodItem> recyclerViewFoodItems = mainInventory.inventory;

        // sort list before dislaying
        mainInventory.sortByExpiry();

        this.recyclerViewFoodItems = (RecyclerView) findViewById(R.id.recyclerView2);

        //not sure why we made another ListManager that is a copy
        // **ListManager fakeInventory = new ListManager(BuildFakeInventory.buildFakeInventory());

        Log.d(TAG, "Now setting up adapter");

        //changed to mainInventory **adapter = new FoodItemAdapter(fakeInventory.inventory);
        adapter = new FoodItemAdapter(mainInventory.inventory, this);

        this.recyclerViewFoodItems.setAdapter(adapter);
        Log.d(TAG, "Now calling layout manager");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.recyclerViewFoodItems.setLayoutManager(mLayoutManager);

        /*NOTE: To update list, use this code onStart (similar to what is present)
            foodItems.clear(); //your food item list used throughout, or get it from ListManager
            //work with adding items from adapter
            adapter.notifyDataSetChanged(); //just notify the data about the change.
            //Stack Overflow post with examples of working with updating RecyclerView Lists:
            //https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
         */

        //END Recycler View Active Code

        // Read from the database

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

                //check that inventory foodItems is updated
                Log.d(TAG, "Size of inventory's food items after clear: " + mainInventory.inventory.size());

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting item
                    FoodItem foodItem = postSnapshot.getValue(FoodItem.class);
                    //adding item to the list
                    foodItems.add(foodItem);

                    Log.d(TAG, "Food item date right after database call: "+foodItem.getFormattedDate());

                    //check what given expiration dates are
                    //Log.d(TAG, "Expiration date of item :")

                }
                Log.d(TAG, "Size of inventory's food items after database add in: " + mainInventory.inventory.size());

                mainInventory.sortByExpiry();

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onSearch(View view) {
        // needs to  get name from view
        String search = "milk";

        ListManager working = new ListManager(mainInventory.inventory);
        foodItems.clear();
        foodItems = working.searchByName(search);
        adapter.notifyDataSetChanged();

    }


    private void editItem() {
        //this will be handled in its own activity
    }

    public void onAdd(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void onSort(View view) {
        //this will be handled in its own activity
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //will clear current reyclcer view of objects and re-display them; call after list updates
    public void resetRecyclerView() {
        //however data is passed:
        //data.clear();
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void onListItemClick(int position) {
        FoodItem foodItem = foodItems.get(position);
        Log.d("Click", foodItem.getName());
        showUpdateDeleteDialog(foodItem);

    }
    private void showUpdateDeleteDialog(FoodItem foodItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialogue, null);
        dialogBuilder.setView(dialogView);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);

        dialogBuilder.setTitle(foodItem.getName());
        final AlertDialog b = dialogBuilder.create();
        b.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToEditItem(foodItem);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(foodItem.getId());
                b.dismiss();
            }
        });
    }
    private boolean deleteItem(String id) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("foodItemsList").child(id);
        //removing item
        dR.removeValue();
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    private void switchToEditItem(FoodItem foodItem) {
        //creating an intent
        Intent switchToEditItemIntent = new Intent(this, EditItemActivity.class);
        //adding item data intent
        int d = foodItem.getDate().getDay();
        int m = foodItem.getDate().getMonth();
        int y = foodItem.getDate().getYear();
        Log.d("date", foodItem.getFormattedDate());
        Log.d("date","day:"+d +" month:" + m + " year:" + y);

        switchToEditItemIntent.putExtra("itemId", foodItem.getId());
        switchToEditItemIntent.putExtra("name", foodItem.getName());
        switchToEditItemIntent.putExtra("day", foodItem.getDate().getDay());
        switchToEditItemIntent.putExtra("month", foodItem.getDate().getMonth());
        switchToEditItemIntent.putExtra("year", foodItem.getDate().getYear());
        switchToEditItemIntent.putExtra("category", foodItem.getFoodType());
        //starting the edit activity with intent
        Log.d(TAG, "Switching to Edit Item Activity");
        startActivity(switchToEditItemIntent);
    }

}
