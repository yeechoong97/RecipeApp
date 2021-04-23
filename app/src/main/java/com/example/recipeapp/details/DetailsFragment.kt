package com.example.recipeapp.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    lateinit var detailsViewModel: DetailsViewModel
    var recipeID : Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDetailsBinding>(inflater,R.layout.fragment_details,container,false)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val database = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val args = DetailsFragmentArgs.fromBundle(requireArguments())
        recipeID = args.recipeID
        val viewModelFactory = DetailsViewModelFactory(application,database,recipeID)
        detailsViewModel = ViewModelProviders.of(this,viewModelFactory).get(DetailsViewModel::class.java)
        binding.lifecycleOwner= this

        //Append the data of Recipe object into respective text view
        detailsViewModel.recipeItem.observe(viewLifecycleOwner, Observer{
            binding.detailsLabelIngredients.text = it.ingredients
            binding.detailsLabelRecipeName.text = it.name
            binding.detailsLabelSteps.text = it.steps
            binding.detailsLabelTypes.text = it.type
            binding.imageView.setImageURI(it.image.toUri())
        })

        //Navigate to Home Fragment if Delete action is confirmed
        detailsViewModel.deleteStatus.observe(viewLifecycleOwner,Observer{
            if(it==true){
                this.findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToFragmentHome())
                val builder = AlertDialog.Builder(this.activity)
                builder.setTitle("Successfully")
                    .setMessage("Recipe is Removed Successfully!")
                    .setPositiveButton("OK") { _, _ ->
                        detailsViewModel.deleteSuccessfulCompleted()
                    }
                builder.show()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.editAction->{
                this.findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToEditFragment(recipeID))
                true
            }
            R.id.deleteAction->{
                deleteConfirmation()
                true
            }
            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    //Prompt confirmation for delete action
    private fun deleteConfirmation(){
        val builder = AlertDialog.Builder(this.activity)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Yes"){_, _ ->
                detailsViewModel.deleteRecipe(recipeID)
            }
            .setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

}