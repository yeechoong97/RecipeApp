package com.example.recipeapp.add

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.database.RecipeDatabaseDao
import com.example.recipeapp.validateInput
import kotlinx.coroutines.*

class AddViewModel (application: Application,private val database: RecipeDatabaseDao) :AndroidViewModel(application){

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _uploadImage = MutableLiveData<Boolean>()
    val uploadImage: LiveData<Boolean>
    get() = _uploadImage

    private var _addRecipe = MutableLiveData<Boolean>()
    val addRecipe :LiveData<Boolean>
    get() = _addRecipe

    private var _addSuccessful = MutableLiveData<Boolean>()
    val addSuccessful :LiveData<Boolean>
    get() = _addSuccessful

    private var _addStatus = MutableLiveData<Boolean>()
    val addStatus :LiveData<Boolean>
    get() = _addStatus

    fun onClick(){
        _uploadImage.value = true
    }

    fun uploadCompleted(){
        _uploadImage.value = false
    }

    fun onAdd(){
        _addRecipe.value = true
    }

    private fun onAddCompleted(){
        _addRecipe.value = false
    }

    private fun recipeAddedSuccessful(){
        _addSuccessful.value = true
    }

    fun recipeAddedSuccessfulCompleted(){
        _addSuccessful.value = false
    }

    private fun addStatusFailed(){
        _addStatus.value = false
    }

    private fun addStatusSuccess(){
        _addStatus.value = true
    }


    fun addRecipe(item: Recipe){
        uiScope.launch {
            if(validateInput(item)){
                addStatusSuccess()
                add(item)
                onAddCompleted()
                recipeAddedSuccessful()
            }
            else{
                addStatusFailed()
            }
        }
    }

    private suspend fun add(item:Recipe){
        withContext(Dispatchers.IO){
            database.insert(item)
        }
    }


}