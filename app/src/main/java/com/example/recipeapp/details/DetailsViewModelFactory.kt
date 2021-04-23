package com.example.recipeapp.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.database.RecipeDatabaseDao
import java.lang.IllegalArgumentException

class DetailsViewModelFactory (val application: Application,val database:RecipeDatabaseDao,var recipeID:Long):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            return DetailsViewModel(application,database,recipeID) as T
        }
        throw IllegalArgumentException("Invalid ViewModel")
    }
}