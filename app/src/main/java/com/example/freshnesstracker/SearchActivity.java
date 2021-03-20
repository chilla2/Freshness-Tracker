package com.example.freshnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {
    private static String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText editTextQuery = (EditText) findViewById(R.id.editTextQuery);

        Button cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain();
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String queryText = editTextQuery.getText().toString().trim();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        //Where the search operation is done.
    }

    private void switchToMain() {
        Intent switchToMainIntent = new Intent(this, MainActivity.class);
        Log.d(TAG, "Switching to main activity");
        startActivity(switchToMainIntent);
    }
}