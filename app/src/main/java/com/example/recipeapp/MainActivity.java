package com.example.recipeapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.recipeapp.RecipeDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final int ADD_RECIPE_REQUEST = 1;

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList;
    private FloatingActionButton fabAddRecipe;
    private RecipeDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = preferences.getString("App_Lang", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        recyclerView = findViewById(R.id.recyclerView);
        fabAddRecipe = findViewById(R.id.fabAddRecipe);
        dbHelper = new RecipeDbHelper(this);

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(this, recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadRecipes(); // Load recipes from DB

        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddEditRecipeActivity when FAB is clicked
                Intent intent = new Intent(MainActivity.this, AddEditRecipeActivity.class);
                startActivityForResult(intent, ADD_RECIPE_REQUEST);
            }
        });
    }

    private void loadRecipes() {
        recipeList.clear();
        recipeList.addAll(dbHelper.getAllRecipes());
        if (recipeList.isEmpty()) {
            addDefaultRecipes();
            recipeList.addAll(dbHelper.getAllRecipes());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_RECIPE_REQUEST && resultCode == RESULT_OK) {
            // Recipe was added/edited, reload the list
            loadRecipes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.lang_en) {
            setLocale("en");
            return true;
        }

        if (item.getItemId() == R.id.lang_ar) {
            setLocale("ar");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setLocale(String lang) {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("App_Lang", lang);
        editor.apply();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    private void addDefaultRecipes() {
        dbHelper.addRecipe(new Recipe("تشيكن ناجتس",
                "حضر الخليط ثم اقلي القطع",
                "1. دجاج\n2. دقيق\n3. بيض\n4. خبز\n5. بهارات"));

        dbHelper.addRecipe(new Recipe("فطيرة التفاح",
                "اخلط التفاح مع السكر ثم اخبز",
                "1. تفاح\n2. سكر\n3. قرفة\n4. عجينة"));

        dbHelper.addRecipe(new Recipe("بيتزا",
                "سخن الفرن واخبز 20 دقيقة",
                "1. عجينة\n2. صلصة\n3. جبنة"));

        dbHelper.addRecipe(new Recipe("فطائر محشوة",
                "افرد العجينة وضع الحشوة ثم اخبزها",
                "1. عجينة\n2. لحمة مفرومة\n3. بصل\n4. بهارات\n5. جبنة"));
        dbHelper.addRecipe(new Recipe("طاجن لحم",
                "ضع اللحم مع الخضار في الطاجن واخبزه",
                "1. لحم\n2. بطاطس\n3. بصل\n4. طماطم\n5. بهارات"));

        dbHelper.addRecipe(new Recipe("شاورما دجاج",
                "قلب قطع الدجاج مع البهارات ثم قدمها",
                "1. دجاج\n2. بهارات شاورما\n3. صلصة طحينة\n4. خبز"));

        dbHelper.addRecipe(new Recipe("محشي ورق عنب",
                "احشي الورق ثم اسلقه",
                "1. ورق عنب\n2. أرز\n3. لحم مفروم\n4. بصل\n5. طماطم"));

        dbHelper.addRecipe(new Recipe("مكرونة",
                "اسلق المكرونة واضف الصلصة",
                "1. مكرونة\n2. صلصة طماطم"));

        dbHelper.addRecipe(new Recipe("بيض بالجبن",
                "اقلي البيض وضع عليه الجبنة",
                "1. بيض\n2. جبنة\n3. ملح وفلفل"));

        dbHelper.addRecipe(new Recipe("سلطة خضراء",
                "اخلط المكونات معاً وأضف الصلصة",
                "1. خس\n2. طماطم\n3. خيار\n4. زيت زيتون\n5. عصير ليمون"));

        dbHelper.addRecipe(new Recipe("شوربة عدس",
                "اغلي العدس مع الخضار وتوابل",
                "1. عدس\n2. جزر\n3. بصل\n4. كمون\n5. ملح"));

        dbHelper.addRecipe(new Recipe("دجاج مشوي",
                "تبّل الدجاج بالبهارات ثم اشويه",
                "1. دجاج\n2. ملح\n3. فلفل أسود\n4. عصير ليمون\n5. زيت زيتون"));

        dbHelper.addRecipe(new Recipe("سمك مقلي",
                "اغسل السمك ثم اقليه في الزيت",
                "1. سمك\n2. دقيق\n3. ملح\n4. فلفل\n5. زيت"));

        dbHelper.addRecipe(new Recipe("كفتة مشوية",
                "اعمل الكفتة على شكل أصابع ثم اشويها",
                "1. لحم مفروم\n2. بصل\n3. بهارات\n4. طماطم\n5. فلفل"));

        dbHelper.addRecipe(new Recipe("كوسا محشي",
                "احشي الكوسا ثم اسلقها",
                "1. كوسا\n2. أرز\n3. لحم مفروم\n4. بصل"));
    }


}




