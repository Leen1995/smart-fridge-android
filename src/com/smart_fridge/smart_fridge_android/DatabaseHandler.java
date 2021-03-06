package com.smart_fridge.smart_fridge_android;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "smartFridge";

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FOOD = "food";
    private static final String TABLE_RECIPES = "recipes";

    // Users table column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD_HASH = "password_hash";
    private static final String KEY_USER_LOGGED_IN = "logged_in";

    // Food table column names
    private static final String KEY_FOOD_ID = "food_id";
    private static final String KEY_FOOD_NAME = "name";
    private static final String KEY_FOOD_DESCRIPTION = "description";
    private static final String KEY_FOOD_CATEGORY = "category";
    private static final String KEY_FOOD_EXPIRATION_DATE = "expiration_date";
    private static final String KEY_FOOD_CALORIES = "calories";
    private static final String KEY_FOOD_QUANTITY = "quantity";
    private static final String KEY_FOOD_PHOTO = "photo";

    // Recipe table column names
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_DIRECTIONS = "directions";
    private static final String KEY_RECIPE_NOTES = "notes";
    private static final String KEY_RECIPE_INGREDIENTS = "ingredients";
    private static final String KEY_RECIPE_PHOTO = "photo";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE "
                + TABLE_USERS
                + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD_HASH + " CHAR(64),"
                + KEY_USER_LOGGED_IN + " BOOLEAN"
                + ")";

        String CREATE_FOOD_TABLE = "CREATE TABLE "
                + TABLE_FOOD
                + "("
                + KEY_FOOD_ID + " INTEGER PRIMARY KEY,"
                + KEY_FOOD_NAME + " TEXT,"
                + KEY_FOOD_DESCRIPTION + " TEXT,"
                + KEY_FOOD_EXPIRATION_DATE + " TEXT,"
                + KEY_FOOD_CATEGORY + " TEXT,"
                + KEY_FOOD_CALORIES + " INT(16),"
                + KEY_FOOD_QUANTITY + " INT(16),"
                + KEY_FOOD_PHOTO + " TEXT" // Not sure if this is the correct type -carl
                + ")";

        String CREATE_RECIPES_TABLE = "CREATE TABLE "
                + TABLE_RECIPES
                + "("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPE_NAME + " TEXT,"
                + KEY_RECIPE_DIRECTIONS + " TEXT,"
                + KEY_RECIPE_NOTES + " TEXT,"
                + KEY_RECIPE_INGREDIENTS + " TEXT,"
                + KEY_RECIPE_PHOTO + " TEXT"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Food
     */

    // Adding new food
    public Food addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getName()); // Food Name
        values.put(KEY_FOOD_DESCRIPTION, food.getDescription()); // Food Description
        values.put(KEY_FOOD_EXPIRATION_DATE, food.getExpirationDate()); // Food Expiration Date
        values.put(KEY_FOOD_CATEGORY, food.getCategory()); // Food Category
        values.put(KEY_FOOD_CALORIES, food.getCalories()); // Food Calories
        values.put(KEY_FOOD_QUANTITY, food.getQuantity()); // Food Quantity
        values.put(KEY_FOOD_PHOTO, food.getImagePath()); //Food Image File Path

        // Inserting Row
        long insertId = db.insert(TABLE_FOOD, null, values);

        Food returnFood = getFoodById((int) insertId);
        db.close(); // Closing database connection
        return returnFood;
    }

    // Getting Food by Id
    public Food getFoodById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOOD, new String[]{KEY_FOOD_ID,
                        KEY_FOOD_NAME, KEY_FOOD_DESCRIPTION, KEY_FOOD_EXPIRATION_DATE,
                        KEY_FOOD_CATEGORY, KEY_FOOD_CALORIES, KEY_FOOD_QUANTITY, KEY_FOOD_PHOTO},
                KEY_FOOD_ID + "= ?", // '?' is replaced by selection args
                new String[]{String.valueOf(id)}, null, null, null, null
        );

        if(cursor!=null&&cursor.moveToFirst())

        {

            Food food = new Food(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                    Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));
            food.setId(Integer.parseInt(cursor.getString(0))); // Set the id
            // return food

            return food;
        }

        return null;
    }


    // Getting All Food
    public List<Food> getAllFood() {
        List<Food> foodList = new ArrayList<Food>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setId(Integer.parseInt(cursor.getString(0)));
                food.setName(cursor.getString(1));
                food.setDescription(cursor.getString(2));
                food.setExpirationDate(cursor.getString(3));
                food.setCategory(cursor.getString(4));
                food.setCalories(cursor.getInt(5));
                food.setQuantity(cursor.getInt(6));
                food.setImagePath(cursor.getString(7));

                // Adding food to list
                foodList.add(food);
            } while (cursor.moveToNext());
        }

        // return food list
        return foodList;
    }

    // Updating Food
    public int updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getName());
        values.put(KEY_FOOD_DESCRIPTION, food.getDescription());
        values.put(KEY_FOOD_EXPIRATION_DATE, food.getExpirationDate());
        values.put(KEY_FOOD_CATEGORY, food.getCategory());
        values.put(KEY_FOOD_CALORIES, food.getCalories());
        values.put(KEY_FOOD_QUANTITY, food.getQuantity());
        values.put(KEY_FOOD_PHOTO, food.getImagePath());

        // updating row
        return db.update(TABLE_FOOD, values, KEY_FOOD_ID + " = ?",
                new String[] { String.valueOf(food.getId()) });
    }

    // Deleting food
    public void deleteFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, KEY_FOOD_ID + " = ?",
                new String[] { String.valueOf(food.getId()) });
        db.close();
    }

    public void deleteAllFood(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FOOD);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Recipes
     */

    // Adding new Recipe
    public Recipe addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_NAME, recipe.getName()); // Recipe Name
        values.put(KEY_RECIPE_DIRECTIONS, recipe.getDirections()); // Recipe Directions
        values.put(KEY_RECIPE_NOTES, recipe.getNotes()); // Recipe Notes
        values.put(KEY_RECIPE_INGREDIENTS, recipe.getIngredients()); // Recipe Ingredients
        values.put(KEY_RECIPE_PHOTO, recipe.getImagePath()); //Recipe Image File Path

        // Inserting Row
        long insertId = db.insert(TABLE_RECIPES, null, values);

        // Inserting Row
        Recipe returnRecipe = getRecipeById((int) insertId);

        db.close(); // Closing database connection
        return returnRecipe;
    }

    // Getting Recipe
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[]{KEY_RECIPE_ID,
                        KEY_RECIPE_NAME, KEY_RECIPE_DIRECTIONS, KEY_RECIPE_NOTES, KEY_RECIPE_INGREDIENTS, KEY_RECIPE_PHOTO},
                KEY_RECIPE_ID + "= ?", // '?' is replaced by selection args
                new String[]{String.valueOf(id)}, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {

            Recipe recipe = new Recipe(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            recipe.setId(Integer.parseInt(cursor.getString(0)));
            // return recipe
            return recipe;
        }
        return null;
    }

    // Getting all Recipe
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipesList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setName(cursor.getString(1));
                recipe.setDirections(cursor.getString(2));
                recipe.setNotes(cursor.getString(3));
                recipe.setIngredients(cursor.getString(4));
                recipe.setImagePath(cursor.getString(5));

                // Adding recipe to list
                recipesList.add(recipe);
            } while (cursor.moveToNext());
        }

        // return recipes list
        return recipesList;
    }

    // Updating Recipe
    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_NAME, recipe.getName());
        values.put(KEY_RECIPE_DIRECTIONS, recipe.getDirections());
        values.put(KEY_RECIPE_NOTES, recipe.getNotes());
        values.put(KEY_RECIPE_INGREDIENTS, recipe.getIngredients());
        values.put(KEY_RECIPE_PHOTO, recipe.getImagePath());

        // updating row
        return db.update(TABLE_RECIPES, values, KEY_RECIPE_ID + " = ?",
                new String[] { String.valueOf(recipe.getId()) });
    }

    // Deleting Recipe
    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_RECIPE_ID + " = ?",
                new String[] { String.valueOf(recipe.getId()) });
        db.close();
    }
}
