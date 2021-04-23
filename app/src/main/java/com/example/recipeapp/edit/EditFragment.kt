package com.example.recipeapp.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.add.REQUEST_CODE
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.database.Recipe
import com.example.recipeapp.databinding.FragmentEditBinding
import com.example.recipeapp.hideKeyboard
import com.google.android.material.snackbar.Snackbar


class EditFragment : Fragment() {

    lateinit var editViewModel : EditViewModel
    lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentEditBinding>(inflater,R.layout.fragment_edit,container,false)
        val application = requireNotNull(this.activity).application
        val database = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val args = EditFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = EditViewModelFactory(application,database,args.recipeID)
        editViewModel = ViewModelProviders.of(this,viewModelFactory).get(EditViewModel::class.java)

        binding.lifecycleOwner = this
        binding.editViewModel = editViewModel
        imageView = binding.imageViewEditRecipe
        setHasOptionsMenu(true)

        var typesArray = resources.getStringArray(R.array.recipe_types)

        //Append the selected Recipe object data into respective input field
        editViewModel.recipeItem.observe(viewLifecycleOwner, Observer{
            it.let{
                editViewModel.currentRecipe = it
                binding.editIngredients.setText(it.ingredients)
                binding.editRecipeName.setText(it.name)
                binding.editSpinner.setSelection(setIndex(editViewModel,typesArray,it.type))
                binding.editSteps.setText(it.steps)
                binding.imageViewEditRecipe.setImageURI(it.image.toUri())
                imageUri = it.image.toUri()
            }
        })

        //Send the updated inputs to ViewModel for validate
        editViewModel.editStatus.observe(viewLifecycleOwner,Observer{
            if(it==true){
                var item = Recipe()
                item.name = binding.editRecipeName.text.toString()
                item.type = binding.editSpinner.selectedItem.toString()
                item.ingredients = binding.editIngredients.text.toString()
                item.steps = binding.editSteps.text.toString()
                item.image = imageUri.toString()
                editViewModel.updateRecipe(item)
                binding.root.hideKeyboard()
            }
        })

        //Invoke Intent to take the image from gallery
        editViewModel.uploadImage.observe(viewLifecycleOwner,Observer{
            if(it==true){
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
                editViewModel.uploadImgCompleted()
            }
        })

        //Navigate to Details Fragment if the Recipe is updated successfully
        editViewModel.editSuccessful.observe(viewLifecycleOwner,Observer{
            if(it==true){
                this.findNavController().navigate(EditFragmentDirections.actionEditFragmentToDetailsFragment(args.recipeID))
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Recipe is Updated Successfully",
                    Snackbar.LENGTH_LONG).show()
                editViewModel.uploadImgCompleted()
            }
        })

        //Show snackbar if the input fields are incomplete
        editViewModel.editResult.observe(viewLifecycleOwner,Observer{
            if(it==false){
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Please Fill Up All Input Fields",
                    Snackbar.LENGTH_SHORT).show()
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
        inflater.inflate(R.menu.edit_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.edit_confirm->{
                editViewModel.editConfirm()
                true
            }
            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setIndex(editViewModel: EditViewModel, array :Array<String>, type:String):Int{
        return editViewModel.getIndex(array,type)
    }
}