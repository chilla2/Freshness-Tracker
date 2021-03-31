package com.example.freshnesstracker;

import android.content.Intent;
import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.os.Bundle;
import android.app.AlarmManager ;
import android.app.Notification ;
import android.app.PendingIntent ;
import android.content.Context ;
import android.content.Intent ;
import android.os.Bundle ;
import android.os.SystemClock ;
import android.view.Menu ;
import android.view.MenuItem ;

/**
 * Main Activity displays list of food items and their expiration dates from realtime database -
 * Add button in bottom right corner switches view to add item screen -
 * Clicking item brings up dialog with buttons to add or delete item -
 */
public class MainActivity extends AppCompatActivity implements FoodItemAdapter.ListItemClickListener, SearchView.OnQueryTextListener {

    //private Spinner mSpinner;
    FloatingActionButton addButton;
    TextView tv1;
    TextView welcome;

    //creating one list to contain all items and remains unchanged except when DB is updated, and one list that will be updated depending on needs of view.
    ArrayList<FoodItem> foodItems;
    ArrayList<FoodItem> displayList;

    RecyclerView recyclerViewFoodItems;
    FoodItemAdapter adapter;

    SearchView editSearch;

    DatabaseReference databaseItems;

    private static final String TAG = "MainActivity";
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    public static final String PATH ="Hadassah" ; //Reference for database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Calling onCreate method");
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        //mSpinner = findViewById(R.id.foodType);
        tv1 = (TextView)findViewById(R.id.textView3);
        welcome = (TextView)findViewById(R.id.welcome);

        databaseItems = FirebaseDatabase.getInstance().getReference(PATH);

        foodItems = new ArrayList<>(); //list to store all food items (updated when database changes)
        displayList = new ArrayList<>(); //list to store items in one category only (gets updated in switchToType())

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

    /** onStart sets up two event listeners.
     * One that listens for changes to the database.
     * One that listens for when a food type is selected from the dropdown.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Calling onStart method");

        databaseItems.addValueEventListener(new ValueEventListener() {
            /**onDataChange keeps the food items lists up-to-date by clearing them,
             * then parsing through the database and adding all the items in the database to the lists,
             *  before sorting the lists by expiration date.
             * */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Calling onDataChange method");
                foodItems.clear();
                displayList.clear();
                tv1.setText("All My Food");
                //String sortSelection = mSpinner.getSelectedItem().toString();

                for(DataSnapshot itemsSnapshot : dataSnapshot.getChildren()) {
                    Log.e("Get Data", itemsSnapshot.getValue(FoodItem.class).toString());
                    foodItems.add(itemsSnapshot.getValue(FoodItem.class));
                    displayList.add(itemsSnapshot.getValue(FoodItem.class));
                }
                if (!(foodItems.size() == 0)) {
                    sortByExpiry(foodItems);
                    sortByExpiry(displayList);
                    checkIfExpired(foodItems);
                    checkIfExpired(displayList);
                    displayByType("All");
                    adapter.notifyDataSetChanged();
                }
                else{
                    welcome.setText("Welcome to the Freshness Tracker! Click the add button to start.");
                    displayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        /** onItemSelected takes the selected food type from the dropdown, then passes that food type into displayByType
         * @param parentView
         * @param selectedItemView
         * @param position
         * @param id
         */
        /*
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
         */
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu , menu) ;
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all :
                displayByType("All");
                tv1.setText("All My Food");

                return true;
            case R.id.bake:
                displayByType("Bakery");
                tv1.setText("My Baked Goods");
                return true;
            case R.id. can :
                displayByType("Canned");
                tv1.setText("My Canned Goods");
                return true;
            case R.id.dairy:
                displayByType("Dairy");
                tv1.setText("My Dairy");
                return true;
            case R.id.dry :
                displayByType("Dry Goods");
                tv1.setText("My Dry Goods");
                return true;
            case R.id.meat:
                displayByType("Meat");
                tv1.setText("All My Meat");
                return true;
            case R.id.produce :
                displayByType("Produce");
                tv1.setText("Produce!!");
                return true;
            case R.id.custom :
                displayByType("Custom");
                tv1.setText("My Custom Foods");

                return true;
            default :
                return super .onOptionsItemSelected(item) ;
        }
    }

    /** onListItemClick calls the showUpdateDeleteDialog method, passing in the appropriate item.
     * @param position
     */
    @Override
    public void onListItemClick(int position) {
        FoodItem foodItem = displayList.get(position);
        Log.d("Click", foodItem.getName());
        showUpdateDeleteDialog(foodItem);
    }

    /** showUpdateDeleteDialog is called when a list item is clicked.
     * The dialog provides the update and delete buttons.
     *  If the item quantity is greater than 1, alternative dialog is shown, allowing user to select quantity to be deleted.
     * */
    private void showUpdateDeleteDialog(FoodItem foodItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        Log.d(TAG, "Showing update/delete dialog");
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
                if (foodItem.getQuantity() == 1) {
                    deleteItem(foodItem.getItemId());
                } else {
                    foodItem.setQuantity(foodItem.getQuantity() - 1);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
    }

    /** switchToAddItem changes to the Add Item activity. This is called when the add button is clicked */
    private void switchToAddItem() {
        Intent switchToAddItemIntent = new Intent(this, AddItemActivity.class);
        startActivity(switchToAddItemIntent);
    }

    /** switchToEditItem changes to the Edit Item activity. This is called when the edit button is clicked from the update/delete dialog
     * @param foodItem
     */
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
        switchToEditItemIntent.putExtra("quantity", foodItem.getQuantity());
        //starting the edit activity with intent
        Log.d(TAG, "Switching to Edit Item Activity");
        startActivity(switchToEditItemIntent);
    }

    /** deleteItem removes selected item from the database. The ID is used to locate the correct item in the database */
    private boolean deleteItem(String id) {
        //getting the specified item reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(PATH).child(id);
        //removing item
        dR.removeValue();
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    /** sortByExpiry takes a food items list, then compares the expiration dates of each item and sorts the list from soonest to latest expiration date
     * @param foodItems
     */
    public void sortByExpiry(ArrayList<FoodItem> foodItems) {
        if (foodItems.size() != 0) {
            Collections.sort(foodItems, new Comparator<FoodItem>() {
                public int compare(FoodItem o1, FoodItem o2) {
                    Calendar date1 = Calendar.getInstance();
                    date1.set(o1.year, o1.month, o1.day);
                    Calendar date2 = Calendar.getInstance();
                    date2.set(o2.year, o2.month, o2.day);
                    return date1.compareTo(date2);
                }
            });
        }
    }

    /** checkIfExpired loops through a food items list and compares each item's expiration date to the current date.
     * If an item is expired, the item's isExpired property is set to true.
     * This gets called every time the food items lists are updated.
     * @param foodItems
     */
    private void checkIfExpired(ArrayList<FoodItem> foodItems) {
        for (FoodItem foodItem : foodItems) {
            //generate expiration date from item's day/month/year
            Calendar expirationDate = Calendar.getInstance();
            int month = foodItem.getMonth();
            month -= 1;
            expirationDate.set(foodItem.getYear(), month, foodItem.getDay(), 0, 0, 0);
            //get current date, and set time to 0
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            //check if expiration date is before current date, and set isExpired to the result (true or false)
            foodItem.setIsExpired(expirationDate.before(currentDate));
            Log.d("Date", "currentDate: " + currentDate.getTime() + "  Item's expiry: "+ expirationDate.getTime());
            Log.d(TAG, "Item: " + foodItem.getName() + " is expired: " + foodItem.getIsExpired());
        }
    }

    /** displayByType clears the displayable food items list,
     * then parses through the food items reference list
     * and adds back only the items that are of the appropriate food type.
     * If the sort selection was "all," all items are added back to the displayable list.
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
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        if (displayList.size() < 1){
            //do nothing
        }

        else if (displayList.get(0).isExpired) {
            scheduleNotification(getNotification("You have food that's not fresh! Check use-by dates"), 150000);
        }
        else {
            Calendar expirationDate = Calendar.getInstance();
            int month = displayList.get(0).getMonth();
            month -= 1;
            expirationDate.set(displayList.get(0).getYear(), month, displayList.get(0).getDay(), 10, 0, 0);
            scheduleNotification2(getNotification("You have food that's not fresh! Check use-by dates"), expirationDate.getTimeInMillis());
            Log.d("notification", "set for"+ expirationDate.getTime());
        }

    }private void scheduleNotification2 (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }
    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        displayList.clear();
        if (charText.length() == 0) {
            displayList.addAll(foodItems);
        } else {
            for (FoodItem foodItem : foodItems) {
                if (foodItem.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    displayList.add(foodItem);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}