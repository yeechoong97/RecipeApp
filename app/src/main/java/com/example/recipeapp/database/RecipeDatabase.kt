package com.example.recipeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Recipe::class],version =2 ,exportSchema = false)
abstract class RecipeDatabase : RoomDatabase(){
    abstract val recipeDatabaseDao: RecipeDatabaseDao

    companion object{

        @Volatile
        private var INSTANCE : RecipeDatabase? = null

        fun getInstance(context: Context):RecipeDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecipeDatabase::class.java,
                        "recipe_database"
                    )
                        .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Thread(Runnable { prepopulateDb(context,getInstance(context)) }).start()
                        }
                    })
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun prepopulateDb(context: Context, db:RecipeDatabase){
            var data: MutableList<Recipe> = mutableListOf(
                Recipe(name="Egg Sandwich",type = "Breakfast",ingredients = "1. 8 eggs \n2. ½ cup mayonnaise \n3. 1 teaspoon prepared yellow mustard \n4. ¼ cup chopped green onion \n5. salt and pepper to taste \n6. ¼ teaspoon paprika",steps = "1. Place egg in a saucepan and cover with cold water. Bring water to a boil and immediately remove from heat. Cover and let eggs stand in hot water for 10 to 12 minutes. Remove from hot water, cool, peel and chop. \n\n2. Place the chopped eggs in a bowl, and stir in the mayonnaise, mustard and green onion. Season with salt, pepper and paprika. Stir and serve on your favorite bread or crackers.",image = "android.resource://com.example.recipeapp/drawable/egg_sandwich"),
                Recipe(name="Fried Rice",type = "Lunch",ingredients = "1. 1 ½ cups uncooked jasmine rice \n2. 3 cups water \n3. 2 teaspoons canola oil \n4. 1 (12 ounce) can fully cooked luncheon meat (such as SPAM®), cubed \n5. ½ cup sliced Chinese sweet pork sausage (lup cheong) \n6. 3 eggs, beaten \n7. 2 tablespoons canola oil \n8. 1 (8 ounce) can pineapple chunks, drained \n9. ½ cup chopped green onion \n10. 3 tablespoons oyster sauce \n11. ½ teaspoon garlic powder",steps = "1. Bring the rice and water to a boil in a saucepan over high heat. Reduce heat to medium-low, cover, and simmer until the rice is tender, and the liquid has been absorbed, 20 to 25 minutes. Let the rice cool completely. \n\n2. Heat 2 teaspoons of oil in a skillet over medium heat, and brown the luncheon meat and sausage. Set aside, and pour the beaten eggs into the hot skillet. Scramble the eggs, and set aside. \n\n3. Heat 2 tablespoons of oil in a large nonstick skillet over medium heat, and stir in the rice. Toss the rice with the hot oil until heated through and beginning to brown, about 2 minutes. Add the garlic powder, toss the rice for 1 more minute to develop the garlic taste, and stir in the luncheon meat, sausage, scrambled eggs, pineapple, and oyster sauce. Cook and stir until the oyster sauce coats the rice and other ingredients, 2 to 3 minutes, stir in the green onions, and serve.",image = "android.resource://com.example.recipeapp/drawable/egg_sandwich"),
                Recipe(name="Fried Noodles",type = "Lunch",ingredients = "1. 2 (3 ounce) packages Oriental flavored ramen noodles \n2. 3 eggs, beaten \n3.vegetable oil \n4. 4 green onions, thinly sliced\n5. 1 small carrot, peeled and grated \n6. ½ cup green peas \n7. ¼ cup red bell pepper, minced \n8. 2 tablespoons sesame oil \n9. soy sauce",steps = "1. Boil ramen noodles for 3 minutes, or until softened, without flavor packets. Reserve flavor packets. Drain noodles, and set aside. \n\n2. Heat 1 tablespoon oil in a small skillet. Scramble eggs in a bowl. Cook and stir in hot oil until firm. Set aside. \n\n3.In a separate skillet, heat 1 teaspoon of oil over medium heat. Cook and stir green onions in oil for 2 to 3 minutes, or until softened. Transfer to a separate dish, and set aside. Heat another teaspoon of cooking oil in the same skillet. Cook and stir the the carrots, peas, and bell peppers separately in the same manner, setting each aside when done. \n\n4.Combine 2 tablespoons sesame oil with 1 tablespoon of vegetable oil in a separate skillet or wok. Fry noodles in oil for 3 to 5 minutes over medium heat, turning regularly. Sprinkle soy sauce, sesame oil, and desired amount of reserved ramen seasoning packets over noodles, and toss to coat. Add vegetables, and continue cooking, turning frequently, for another 5 minutes.",image = "android.resource://com.example.recipeapp/drawable/fried_noodles"),
                Recipe(name="Mini Brownies",type = "Dessert",ingredients = "1. 1 package fudge brownie mix\n2. 48 striped or milk chocolate kisses",steps = "1.Prepare brownie mix according to package directions for fudge-like brownies. Fill paper-lined miniature muffin cups two-thirds full. \n\n2. Bake at 350° for 18-21 minutes or until a toothpick inserted in the center comes out clean. \n\n3.Immediately top each with a chocolate kiss. Cool for 10 minutes before removing from pans to wire racks to cool completely.",image = "android.resource://com.example.recipeapp/drawable/mini_brownies")
                )
            db.recipeDatabaseDao.insertAll(data)
        }

    }
}