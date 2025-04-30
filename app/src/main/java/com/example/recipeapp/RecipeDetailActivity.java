package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView ingredientsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        titleTextView = findViewById(R.id.detailTitle);
        descriptionTextView = findViewById(R.id.detailDescription);
        ingredientsTextView = findViewById(R.id.detailIngredients);

        // Get the recipe data from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RECIPE_TITLE") && intent.hasExtra("RECIPE_DESCRIPTION") && intent.hasExtra("RECIPE_INGREDIENTS")) {
            String title = intent.getStringExtra("RECIPE_TITLE");
            String description = intent.getStringExtra("RECIPE_DESCRIPTION");
            String ingredients = intent.getStringExtra("RECIPE_INGREDIENTS");

            // Set the data to the TextViews
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            ingredientsTextView.setText(ingredients);

            // Set the activity title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Add back button
            }
        }
    }

    // Handle the back button press in the action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
