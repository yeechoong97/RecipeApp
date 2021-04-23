package com.example.recipeapp.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.database.RecipeDatabaseDao
import kotlinx.coroutines.*

class DetailsViewModel (application: Application,val database: RecipeDatabaseDao,val recipeID:Long):AndroidViewModel(application){

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipeItem  = database.getRecipe(recipeID)

    private var _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus:LiveData<Boolean>
    get() = _deleteStatus

    private fun deleteSuccessful(){
        _deleteStatus.value = true
    }

    fun deleteSuccessfulCompleted(){
        _deleteStatus.value = false
    }

    private suspend fun delete(id:Long){
        withContext(Dispatchers.IO){
            database.deleteRecord(id)
        }
    }

    fun deleteRecipe(id:Long){
        uiScope.launch {
           delete(id)
            deleteSuccessful()
        }
    }



}