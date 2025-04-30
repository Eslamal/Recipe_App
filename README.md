## 📖 Overview

This is a simple Android application for managing food recipes, developed using **Java** and **XML**. The app allows users to:

- View a list of recipes.
- View detailed information for each recipe.
- Add new recipes.
- Edit existing recipes.
- Delete recipes.

All recipe data is stored **locally** on the device using an **SQLite** database.

---

## 🚀 Features

- **Recipe List Display:** Recipes are displayed on the home screen using `RecyclerView` and `CardView`.
- **View Recipe Details:** Clicking on a recipe opens `RecipeDetailActivity`, showing its title, description, and ingredients.
- **Add New Recipe:** Tap the `FloatingActionButton` to open `AddEditRecipeActivity` where you can input new recipe details.
- **Edit Recipe:** Long-press on a recipe to open a context menu with the “Edit” option, allowing you to update existing recipe information.
- **Delete Recipe:** Long-press on a recipe and choose “Delete”. A confirmation dialog will appear before removing it from the database.
- **Local Storage:** Recipes are saved locally using `SQLiteOpenHelper`.
