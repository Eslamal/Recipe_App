package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.RecipeDbHelper;

public class AddEditRecipeActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextIngredients;
    private Button buttonSave;
    private RecipeDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextIngredients = findViewById(R.id.editTextIngredients);
        buttonSave = findViewById(R.id.buttonSave);
        dbHelper = new RecipeDbHelper(this);

        // Set title and back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.activity_add_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set click listener for save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });
    }

    private void saveRecipe() {
        // Get input values
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String ingredients = editTextIngredients.getText().toString().trim();

        // Validate input
        if (title.isEmpty()) {
            editTextTitle.setError(getString(R.string.title_required));
            editTextTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError(getString(R.string.description_required));
            editTextDescription.requestFocus();
            return;
        }

        if (ingredients.isEmpty()) {
            editTextIngredients.setError(getString(R.string.ingredients_required));
            editTextIngredients.requestFocus();
            return;
        }

        // Create a new Recipe object
        Recipe newRecipe = new Recipe(title, description, ingredients);

        // Save the recipe to the database
        long result = dbHelper.addRecipe(newRecipe);

        if (result != -1) {
            Toast.makeText(this, R.string.successfully, Toast.LENGTH_SHORT).show();
            // Set result to OK to notify MainActivity to refresh the list
            setResult(RESULT_OK);
            finish(); // Close the activity
        } else {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the back button press in the action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
