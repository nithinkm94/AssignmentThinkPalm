package com.example.thinkpalmassignment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thinkpalmassignment.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var activityMainBinding: ActivityMainBinding
    private val columnCountPortrait = 3
    private val columnCountLandscape = 5

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.title = "Romantic Comedy"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModelFactory = MainViewModelFactory(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)
        activityMainBinding.viewModel = viewModel

        //Showing List
        adapter = ItemListAdapter(applicationContext)
        if (resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT
        ) {
            activityMainBinding.rvItems.layoutManager = GridLayoutManager(this, columnCountPortrait)
        } else {
            activityMainBinding.rvItems.layoutManager = GridLayoutManager(this, columnCountLandscape)
        }

        activityMainBinding.rvItems.adapter = adapter
        viewModel.searchPagedListLiveData.observe(this, Observer {
            adapter.submitList(it)
            if (it.size <= 0)
                Toast.makeText(applicationContext, "Sorry No Items Found!", Toast.LENGTH_SHORT)
                    .show()
        })
    }

    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE;
        searchView.queryHint = "Search"
        searchView.isIconified = !viewModel.searchTextMutableLiveData.value?.isNotEmpty()!!
        searchView.setQuery(viewModel.searchTextMutableLiveData.value, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("data", "onSearch    " + newText.toString())
                viewModel.onSearch(newText)
                return true
            }
        }
        )
        return super.onCreateOptionsMenu(menu)
    }

}