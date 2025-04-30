package com.example.recipeapp;

import java.io.Serializable;

// Implement Serializable to pass Recipe objects via Intent
public class Recipe implements Serializable {
    private long id; // Add ID field for database operations
    private String title;
    private String description;
    private String ingredients;
    // Add other fields as needed, e.g., image URL, instructions, cooking time, etc.

    // Constructor without ID (for adding new recipes before DB insertion)
    public Recipe(String title, String description, String ingredients) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
    }

    // Constructor with ID (for recipes retrieved from DB)
    public Recipe(long id, String title, String description, String ingredients) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    // Setters (optional, depending on whether recipes are mutable)
    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
