package com.example.recipeapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDatabaseDao{
    @Insert
    suspend fun insert(item:Recipe)

    @Insert
    fun insertAll(items: MutableList<Recipe>)

    @Update
    suspend fun update(item:Recipe)

    @Query("SELECT * FROM recipe_table ORDER BY types ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table where types = :type")
    fun getListRecipes(type:String): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table where recipeID = :id")
    fun getRecipe(id:Long): LiveData<Recipe>

    @Query("DELETE FROM recipe_table where recipeID = :id")
    suspend fun deleteRecord(id:Long)


}