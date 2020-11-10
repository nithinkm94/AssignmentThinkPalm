package com.example.thinkpalmassignment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.thinkpalmassignment.data.Items
import com.example.thinkpalmassignment.paging.ItemListDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class MainActivityViewModel(private val context: Context) : ViewModel() {
    var searchPagedListLiveData: LiveData<PagedList<Items>>
    private var searchDataSource: ItemListDataSource? = null
    var searchTextMutableLiveData = MutableLiveData<String>()

    init {
        searchTextMutableLiveData.value = ""
        searchPagedListLiveData = initializeSearchListLiveData()
    }

    private fun initializeSearchListLiveData(): LiveData<PagedList<Items>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ItemListDataSource.PAGE_SIZE)
            .build()

        val dataSource = object : DataSource.Factory<Int, Items>() {
            override fun create(): ItemListDataSource {
                return ItemListDataSource(
                    context,
                    searchTextMutableLiveData.value
                ).also {
                    searchDataSource = it
                }
            }
        }

        return LivePagedListBuilder(dataSource, config).build()
    }

    val queryChannel = ConflatedBroadcastChannel<String>()


    fun onSearch(searchText: String?) {
        /**TODO Search on first page item can only be reflected*/
        queryChannel.offer(searchText.toString())
        queryChannel.asFlow()
            .debounce(QUERY_DEBOUNCE)
            .onEach {
                Log.i("data", "debounce    " + searchText.toString())
                /**TODO We can do the local search from here*/
//                doLocalSearch()
                searchDataSource?.invalidate()
                searchTextMutableLiveData.value = searchText.toString()
                searchPagedListLiveData = initializeSearchListLiveData()
            }
            .launchIn(viewModelScope)
    }

    private fun doLocalSearch() {
        /**TODO We can do a local search here and can update the list*/
    }

    companion object {
        private const val QUERY_DEBOUNCE = 1000L
    }


}