package com.example.freshnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
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

import static androidx.recyclerview.widget.RecyclerView.*;


public class MainActivity extends AppCompatActivity implements FoodItemAdapter.ListItemClickListener {
    ListManager mainInventory;
    private Spinner mSpinner;

    //our database reference object
    DatabaseReference databaseItems;
    ArrayList<FoodItem> foodItems;

    //RecyclerView Declarations
    RecyclerView recyclerViewFoodItems;
    Adapter adapter;

    private static final String TAG = "MainActivity";
    final public static String PATH = "foodItemsList";
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

        final Button buttonOK = (Button) findViewById(R.id.OK);
      /*  buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToType();
            }
        });*/

        //getting the reference of FoodItem node
        databaseItems = FirebaseDatabase.getInstance().getReference(PATH);

        //START: Recycler View implementation with Fake Inventory List made in onCreate
        //TO DO: Put in onStart and get list from FIREBASE instead

        // Changed this name to foodItems to merge different codes and initiated the property with fake inventory

        foodItems = new ArrayList<FoodItem>();
        Log.d(TAG, "In on create start");
        // initiated with the variable instead of hardcode

        this.mainInventory = new ListManager(foodItems);
        this.recyclerViewFoodItems = (RecyclerView) findViewById(R.id.recyclerView2);
        Log.d(TAG, "Now setting up adapter");
        adapter = new FoodItemAdapter(mainInventory.inventory, this);
        this.recyclerViewFoodItems.setAdapter(adapter);
        Log.d(TAG, "Now calling layout manager");
        LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.recyclerViewFoodItems.setLayoutManager(mLayoutManager);

        /*NOTE: To update list, use this code onStart (similar to what is present)
            foodItems.clear(); //your food item list used throughout, or get it from ListManager
            //work with adding items from adapter
            adapter.notifyDataSetChanged(); //just notify the data about the change.
            //Stack Overflow post with examples of working with updating RecyclerView Lists:
            //https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
         */

        //END Recycler View Active Code
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
                mSpinner = (Spinner) findViewById(R.id.foodType);
                String category = mSpinner.getSelectedItem().toString();
                String all = "All";
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = postSnapshot.getValue(FoodItem.class);

                    Log.d("type", "category =" + category + "foodType =" + foodItem.getFoodType() + "!");
                    if (all.equals(category)) {
                        foodItems.add(foodItem);
                    } else if (foodItem.getFoodType().equals(category)) {
                        //adding item to the list
                        foodItems.add(foodItem);
                    }
                    mainInventory.sortByExpiry();

                    Log.d(TAG, "Food item date right after database call: " + foodItem.getFormattedDate());

                    //check what given expiration dates are
                    //Log.d(TAG, "Expiration date of item :")

                }
                Log.d(TAG, "Size of inventory's food items after database add in: " + mainInventory.inventory.size());
                ArrayList<FoodItem> temp = (ArrayList<FoodItem>) foodItems.clone();
                mainInventory = new ListManager(temp);
                mainInventory.sortByExpiry();

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button buttonOK = (Button) findViewById(R.id.OK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToType();
                //adapter.notifyDataSetChanged();
                //foodItems.clear();
                //recyclerViewFoodItems.invalidate();
            }
        });
//         mSpinner = (Spinner) findViewById(R.id.foodType);
//
//
//
//         // Initialize an array adapter
//         //mAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,flowers);
//         //mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//
//         // Data bind the spinner with array adapter items
//         //mSpinner.setAdapter(mAdapter);
//         String category = mSpinner.getSelectedItem().toString();
//
//
//         // Set an item selection listener for spinner widget
//         mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//             /*
//                 onItemSelected
//                     void onItemSelected (AdapterView<?> parent,
//                                     View view,
//                                     int position,
//                                     long id)
//
//                     Callback method to be invoked when an item in this view has been selected.
//                     This callback is invoked only when the newly selected position is different
//                     from the previously selected position or if there was no selected item.
//
//                     Impelmenters can call getItemAtPosition(position) if they need to access the
//                     data associated with the selected item.
//
//                     Parameters
//                         parent AdapterView: The AdapterView where the selection happened
//                         view View: The view within the AdapterView that was clicked
//                         position int: The position of the view in the adapter
//                         id long: The row id of the item that is selected
//             */
//             @Override
//             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                 // Get the spinner selected item text
//                 String category = (String) adapterView.getItemAtPosition(i);
//                 if(category == "Dairy"){
//                     switchToTypeDairy();
//                 }
//
//
//                 // Display the selected item into the TextView
//                 // mTextView.setText("Selected : " + selectedItemText);
//             }
//
//             /*
//                 onNothingSelected
//
//                     void onNothingSelected (AdapterView<?> parent)
//                     Callback method to be invoked when the selection disappears from this view.
//                     The selection can disappear for instance when touch is activated or when
//                     the adapter becomes empty.
//
//                     Parameters
//                         parent AdapterView: The AdapterView that now contains no selected item.
//             */
//             @Override
//             public void onNothingSelected(AdapterView<?> adapterView) {
//                 // Toast.makeText(mContext,"No selection",Toast.LENGTH_LONG).show();
//             }
//         });


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

        adapter.notifyDataSetChanged();
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
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(PATH).child(id);
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
        Log.d("date", "day:" + d + " month:" + m + " year:" + y);

        switchToEditItemIntent.putExtra("itemId", foodItem.getId());
        switchToEditItemIntent.putExtra("name", foodItem.getName());
        switchToEditItemIntent.putExtra("day", foodItem.getDay());
        switchToEditItemIntent.putExtra("month", foodItem.getMonth());
        switchToEditItemIntent.putExtra("year", foodItem.getYear());
        switchToEditItemIntent.putExtra("category", foodItem.getFoodType());
        //starting the edit activity with intent
        Log.d(TAG, "Switching to Edit Item Activity");
        startActivity(switchToEditItemIntent);
    }

    private void switchToType() {

        int tempDay= foodItems.get(0).getDay();
        int tempMonth= foodItems.get(0).getMonth();
        int tempYear= foodItems.get(0).getYear();
        String tempName = foodItems.get(0).getName();
        String tempType = foodItems.get(0).getFoodType();
        String id = databaseItems.push().getKey();
        deleteItem(foodItems.get(0).getId());

        FoodItem food = new FoodItem("123"    , 01, 01, 07, "Sample", "Custom");
        databaseItems.child(id).setValue(food);


        //mSpinner = (Spinner) findViewById(R.id.foodType);
        //String category = mSpinner.getSelectedItem().toString();

        //adapter.notifyDataSetChanged();


        //creating an intent
        //Intent typeIntent = new Intent(this, TypeDairyActivity.class);
        //typeIntent.putExtra("cat", category);
        //startActivity(typeIntent);
    }


    }

