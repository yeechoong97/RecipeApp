package com.example.recipeapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recipeapp.databinding.ActivityMainBinding

const val STORAGE_CODE = 101
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
        checkForPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_CODE)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    private fun checkForPermissions(permission:String,requestCode: Int){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_DENIED->{
                    ActivityCompat.requestPermissions(this,arrayOf(permission),requestCode)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name:String){
            if(grantResults.isEmpty() || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"$name Permission Refused",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"$name Permission Granted",Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode){
            STORAGE_CODE -> innerCheck("storage")
        }
    }

}