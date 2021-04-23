package com.example.recipeapp.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    var recipeID: Long = 0L,

    @ColumnInfo(name="name")
    var name: String = "",

    @ColumnInfo(name="types")
    var type: String = "",

    @ColumnInfo(name="ingredients")
    var ingredients: String = "",

    @ColumnInfo(name="steps")
    var steps: String = "",

    @ColumnInfo(name="image")
    var image: String = ""
)