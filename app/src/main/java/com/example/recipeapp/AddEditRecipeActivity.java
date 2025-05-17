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
    private int recipeId = -1; // Default to -1, meaning no recipe is selected for editing

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

        // Get the passed Recipe object from Intent
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("EDIT_RECIPE");

        if (recipe != null) {
            // Fill the EditTexts with current data
            editTextTitle.setText(recipe.getTitle());
            editTextDescription.setText(recipe.getDescription());
            editTextIngredients.setText(recipe.getIngredients());
        }

        // Set title and back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe != null ? R.string.activity_edit_title : R.string.activity_add_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set click listener for save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe(recipe);  // Pass the recipe to save method
            }
        });
    }

    private void saveRecipe(Recipe recipe) {
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

        // Set the fields of the recipe object before updating or adding it
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);

        // Update or add the recipe
        if (recipe.getId() != -1) {
            int result = dbHelper.updateRecipe(recipe);
            if (result > 0) {
                Toast.makeText(this, R.string.successfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = dbHelper.addRecipe(recipe);
            if (result != -1) {
                Toast.makeText(this, R.string.successfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        }

        setResult(RESULT_OK);
        finish();
    }



    // Handle the back button press in the action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
