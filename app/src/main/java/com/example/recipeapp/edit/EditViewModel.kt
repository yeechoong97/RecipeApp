package com.example.recipeapp.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.database.RecipeDatabaseDao
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.validateInput
import kotlinx.coroutines.*

class EditViewModel(application: Application,val database: RecipeDatabaseDao,val recipeID: Long): AndroidViewModel(application){

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipeItem = database.getRecipe(recipeID)
    lateinit var currentRecipe: Recipe

    private var _uploadImage = MutableLiveData<Boolean>()
    val uploadImage :LiveData<Boolean>
    get() = _uploadImage

    private var _editStatus = MutableLiveData<Boolean>()
    val editStatus : LiveData<Boolean>
    get() = _editStatus

    private var _editSuccessful = MutableLiveData<Boolean>()
    val editSuccessful :LiveData<Boolean>
    get() = _editSuccessful

    private var _editResult = MutableLiveData<Boolean>()
    val editResult :LiveData<Boolean>
    get() = _editResult

    fun uploadImg(){
        _uploadImage.value = true
    }

    fun uploadImgCompleted(){
        _uploadImage.value = false
    }


    fun editConfirm(){
        _editStatus.value = true
    }

    private fun editConfirmComplete(){
        _editStatus.value = false
    }


    private fun editSuccess(){
        _editSuccessful.value = true
    }

    private fun editValidateSuccess(){
        _editResult.value = true
    }

    private fun editValidateFailed(){
        _editResult.value = false
    }

    //Retrieve the index for the selected type
    fun getIndex(array:Array<String>,type:String):Int{
        var index = 0
        for(i in array.indices){
            if(array[i] == type){
                index = i
                break
            }
        }
        return index
    }

    private suspend fun update(item:Recipe){
        withContext(Dispatchers.IO){
            database.update(item)
        }
    }

    fun updateRecipe(item:Recipe){
        uiScope.launch {
            if(validateInput(item)){
                currentRecipe.image = item.image
                currentRecipe.ingredients= item.ingredients
                currentRecipe.steps = item.steps
                currentRecipe.name = item.name
                currentRecipe.type = item.type
                editValidateSuccess()
                update(currentRecipe)
                editConfirmComplete()
                editSuccess()
            }else{
                editValidateFailed()
            }
        }
    }

}