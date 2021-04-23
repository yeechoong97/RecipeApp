package com.example.recipeapp.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.database.RecipeDatabaseDao
import java.lang.IllegalArgumentException

class EditViewModelFactory (val application: Application, val database: RecipeDatabaseDao,val recipeID: Long): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditViewModel::class.java)){
            return EditViewModel(application,database,recipeID) as T
        }
        throw IllegalArgumentException("Invalid ViewModel")
    }
}