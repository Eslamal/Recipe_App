package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.recipeapp.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 4;

    // Table and column names
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_INGREDIENTS = "ingredients";

    // Create table statement
    private static final String SQL_CREATE_RECIPES_TABLE =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_INGREDIENTS + " TEXT)";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // Add a new recipe
    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return newRowId;
    }

    // Get all recipes
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));

                Recipe recipe = new Recipe(id, title, description, ingredients);
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipeList;
    }

    // Update a recipe
    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());

        // Update the recipe where the ID matches
        return db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipe.getId())});
    }



    // Delete a recipe
    public int deleteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the row, returning the number of rows affected
        int rowsAffected = db.delete(TABLE_RECIPES, COLUMN_ID + " = ?",
                new String[]{String.valueOf(recipeId)});
        db.close();
        return rowsAffected;
    }

    // Get a single recipe by ID
    public Recipe getRecipe(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_INGREDIENTS},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS))
            );
            cursor.close();
        }
        db.close();
        return recipe;
    }
}
