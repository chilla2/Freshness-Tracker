package com.example.freshnesstracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;


import androidx.appcompat.app.AppCompatActivity;




public class MainActivity extends AppCompatActivity {
    ListManager mainInventory;
    ArrayAdapter<String> arrayAdapter;
    private static final String FILE_NAME = "food-item-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void onAddFoodItem(View view){
    }
    private void onSearch(View view){
    }



}