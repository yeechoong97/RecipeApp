package com.example.recipeapp.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.databinding.RecipeListBinding

class HomeAdapter(private val clickListener:ItemListener): RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    var data = listOf<Recipe>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        return holder.bind(data,clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: RecipeListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Recipe,clickListener: ItemListener){
            binding.textViewRecipeName.text = data.name
            binding.textViewRecipeTypes.text = "Type: ${data.type}"
//            var img = "android.resource://com.example.recipeapp/drawable/egg_sandwich"
            binding.imageRecipe.setImageURI(data.image.toUri())
            binding.clickListener = clickListener
            binding.recipe = data
        }

        companion object{
            fun from(parent:ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeListBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class ItemListener(val clickListener: (item:Recipe)->Unit){
    fun onClick(item:Recipe) = clickListener(item)
}