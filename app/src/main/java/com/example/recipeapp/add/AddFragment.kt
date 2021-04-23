package com.example.recipeapp.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.FragmentAddBinding
import com.example.recipeapp.hideKeyboard
import com.google.android.material.snackbar.Snackbar

const val REQUEST_CODE = 100
class AddFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var addViewModel: AddViewModel
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddBinding>(inflater,
            R.layout.fragment_add,container,false)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val database = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val viewModelFactory = AddViewModelFactory(application,database)
        addViewModel = ViewModelProviders.of(this,viewModelFactory).get(AddViewModel::class.java)
        binding.lifecycleOwner = this
        binding.addViewModel = addViewModel
        imageView = binding.imageViewRecipe

        //Invoke intent to take image from gallery
        addViewModel.uploadImage.observe(viewLifecycleOwner, Observer{
                if(it==true){
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE)
                    addViewModel.uploadCompleted()
                }
        })

        //Send the recipe class to ViewModel for processing
        addViewModel.addRecipe.observe(viewLifecycleOwner,Observer{
            if(it==true){
                val recipe = Recipe()
                recipe.name = binding.editTextRecipeName.text.toString()
                recipe.type = binding.spinner.selectedItem.toString()
                recipe.ingredients = binding.editTextIngredients.text.toString()
                recipe.steps = binding.editTextSteps.text.toString()
                recipe.image = imageUri.toString()
                addViewModel.addRecipe(recipe)
                binding.root.hideKeyboard()
            }
        })

        //Trigger snackbar notification if input fields are incomplete
        addViewModel.addStatus.observe(viewLifecycleOwner,Observer{
            if(it==false){
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Please Fill Up All Input Fields",Snackbar.LENGTH_SHORT).show()
            }
        })

        //Navigate to home fragment after added recipe successfully
        addViewModel.addSuccessful.observe(viewLifecycleOwner,Observer{
            if(it==true){
                this.findNavController().navigate(AddFragmentDirections.actionAddFragmentToFragmentHome())
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"New Recipe is Added Successful",Snackbar.LENGTH_LONG).show()
                addViewModel.recipeAddedSuccessfulCompleted()
            }
        })
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageView.setImageURI(data?.data)
            imageUri = data?.data!!
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addAction->{
                addViewModel.onAdd()
                true
            }else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}