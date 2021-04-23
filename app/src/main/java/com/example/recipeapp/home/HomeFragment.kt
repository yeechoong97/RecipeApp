package com.example.recipeapp.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val database = RecipeDatabase.getInstance(application).recipeDatabaseDao
        val viewModelFactory = HomeViewModelFactory(application,database)
        homeViewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)
        binding.lifecycleOwner = this
        binding.homeViewModel = homeViewModel

        //Append adapter into recycler list
        val adapter = HomeAdapter(ItemListener {
            this.findNavController().navigate(HomeFragmentDirections.actionFragmentHomeToDetailsFragment(it.recipeID))
        })
        binding.recyclerListRecipe.adapter = adapter

        //Observe the live data and send it to adapter
        homeViewModel.allRecipes.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.data = it
            }
        })

        //Navigate to Add Fragment once add button is clicked
        homeViewModel.addButton.observe(viewLifecycleOwner,Observer{
            if(it==true){
                this.findNavController().navigate(HomeFragmentDirections.actionFragmentHomeToAddFragment())
                homeViewModel.addItemCompleted()
            }
        })

        val layoutManager = LinearLayoutManager(activity , RecyclerView.VERTICAL,false).apply { binding.recyclerListRecipe.layoutManager= this }
        DividerItemDecoration(activity,layoutManager.orientation).apply { binding.recyclerListRecipe.addItemDecoration(this) }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.filter_button->{
                true
            }
            else ->{
                homeViewModel.retrieve(item.title.toString())
                var ft :FragmentTransaction = parentFragmentManager.beginTransaction()
                ft.detach(this).attach(this).commit()
                true
            }
        }

    }


}