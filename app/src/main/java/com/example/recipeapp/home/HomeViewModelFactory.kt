package com.example.recipeapp.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.database.RecipeDatabaseDao
import java.lang.IllegalArgumentException

class HomeViewModelFactory(val application: Application,val database: RecipeDatabaseDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(application,database) as T
        }
        throw IllegalArgumentException("Invalid ViewModel")
    }
}