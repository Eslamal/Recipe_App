package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.RecipeDbHelper;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private RecipeDbHelper dbHelper;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        this.dbHelper = new RecipeDbHelper(context);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.titleTextView.setText(recipe.getTitle());
        holder.descriptionTextView.setText(recipe.getDescription());

        // Set click listener for each item (normal click for details)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Recipe currentRecipe = recipeList.get(currentPosition);
                    // Create intent to open RecipeDetailActivity
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    // Pass recipe data to the detail activity
                    intent.putExtra("RECIPE_TITLE", currentRecipe.getTitle());
                    intent.putExtra("RECIPE_DESCRIPTION", currentRecipe.getDescription());
                    intent.putExtra("RECIPE_INGREDIENTS", currentRecipe.getIngredients());
                    // Start the detail activity
                    context.startActivity(intent);
                }
            }
        });

        // Set long click listener for edit/delete options
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Recipe currentRecipe = recipeList.get(currentPosition);
                    // Show dialog with edit/delete options
                    showOptionsDialog(currentRecipe, currentPosition);
                }
                return true; // Consume the long click event
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // Method to show options dialog for edit/delete
    private void showOptionsDialog(Recipe recipe, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(recipe.getTitle());
        String s1= context.getString(R.string.edit); ;
        String s2= context.getString(R.string.delete ); ;

        String[] options = {s1,s2 };

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Edit
                        editRecipe(recipe);
                        break;
                    case 1: // Delete
                        deleteRecipe(recipe, position);
                        break;
                }
            }
        });

        builder.show();
    }

    // Method to handle edit recipe
    private void editRecipe(Recipe recipe) {
        Intent intent = new Intent(context, AddEditRecipeActivity.class);
        // Pass the recipe object to edit
        intent.putExtra("EDIT_RECIPE", recipe);
        // Start activity for result to refresh list after edit
        if (context instanceof MainActivity) {
            ((MainActivity) context).startActivityForResult(intent, MainActivity.ADD_RECIPE_REQUEST);
        }
    }

    // Method to handle delete recipe
    private void deleteRecipe(Recipe recipe, int position) {
        // Confirm deletion with dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.confirm_delete));
        builder.setMessage(context.getString(R.string.sure)+" " + recipe.getTitle() +" "+ context.getString(R.string.question_mark));

        builder.setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Verify position is still valid before deletion
                if (position >= 0 && position < recipeList.size()) {
                    // Delete from database
                    int result = dbHelper.deleteRecipe(recipe.getId());
                    if (result > 0) {
                        // Remove from list and update adapter
                        recipeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, recipeList.size());
                        Toast.makeText(context, context.getString(R.string.remove_done), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.remove_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.show();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipeTitle);
            descriptionTextView = itemView.findViewById(R.id.recipeDescription);
        }
    }
}
