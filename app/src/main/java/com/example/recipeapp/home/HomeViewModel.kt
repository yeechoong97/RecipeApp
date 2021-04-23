package com.example.recipeapp.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.database.RecipeDatabaseDao
import kotlinx.coroutines.*

class HomeViewModel (application: Application,val database: RecipeDatabaseDao):AndroidViewModel(application){

    private val viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var allRecipes : LiveData<List<Recipe>> = database.getAllRecipes()

    private var _addButton = MutableLiveData<Boolean>()
    val addButton:LiveData<Boolean>
    get() = _addButton

    fun addItem(){
        _addButton.value = true
    }

    fun addItemCompleted(){
        _addButton.value = false
    }

    private suspend fun retrieveRecipe(types:String){
        withContext(Dispatchers.IO){
            allRecipes = database.getListRecipes(types)
        }
    }

    private suspend fun retrieveAllRecipe(){
        withContext(Dispatchers.IO){
            allRecipes = database.getAllRecipes()
        }
    }

    fun retrieve(types: String){
        uiScope.launch {
            if(types=="All"){
                retrieveAllRecipe()
            }else{
                retrieveRecipe(types)
            }
        }
    }

}