package com.example.recipeapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.recipeapp.database.Recipe

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun validateInput(item: Recipe):Boolean{
    return !(item.image=="null" || item.ingredients=="" || item.name=="" || item.steps=="")
}