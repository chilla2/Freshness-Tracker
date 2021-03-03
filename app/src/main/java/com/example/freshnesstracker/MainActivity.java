package com.example.freshnesstracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void onSearch(View view){
        //this will be handled in its own activity
    }

    private void createItem(String name, Date date, FoodType category) {
        //this will be handled in its own activity
    }

    private void editItem() {
        //this will be handled in its own activity
    }
}