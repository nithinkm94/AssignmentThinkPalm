package com.example.thinkpalmassignment.paging

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.thinkpalmassignment.data.ContentList
import com.example.thinkpalmassignment.data.Items
import com.example.thinkpalmassignment.util.getJsonFromAssets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class ItemListDataSource(private val context: Context,private val searchText: String?) : PageKeyedDataSource<Int, Items>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Items>
    ) {

        val jsonFileString = getJsonFromAssets(context, "page$FIRST_PAGE.json")

        val gson = Gson()
        val listPersonType = object : TypeToken<ContentList>() {}.type

        val contentList: ContentList = gson.fromJson(jsonFileString, listPersonType)

        val filteredList = contentList.page.content_items.content.filter {items -> items.name.toLowerCase(
            Locale.ROOT
        ).contains(
            searchText.toString().toLowerCase(Locale.ROOT)
        ) }

        callback.onResult(
            filteredList,
            null,
            FIRST_PAGE + 1
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Items>) {

        val page = params.key
        val jsonFileString = getJsonFromAssets(context, "page$page.json")


        val gson = Gson()
        val listPersonType = object : TypeToken<ContentList>() {}.type

        val contentList: ContentList = gson.fromJson(jsonFileString, listPersonType)
        val filteredList = contentList.page.content_items.content.filter {items -> items.name.toLowerCase(
            Locale.ROOT
        ).contains(
            searchText.toString().toLowerCase(Locale.ROOT)
        ) }

       /**TODO we can also check the page size from the response*/
        callback.onResult(filteredList, if(params.key + 1 <=3) params.key + 1 else null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Items>) {


        val page = params.key
        val jsonFileString = getJsonFromAssets(context, "page$page.json")

        val gson = Gson()
        val listPersonType = object : TypeToken<ContentList>() {}.type

        val contentList: ContentList = gson.fromJson(jsonFileString, listPersonType)
        val key = if (params.key > 1) params.key - 1 else 0

        val filteredList = contentList.page.content_items.content.filter {items -> items.name.toLowerCase(
            Locale.ROOT
        ).contains(
            searchText.toString().toLowerCase(Locale.ROOT)
        ) }

        callback.onResult(filteredList, key)

    }

    companion object {
        const val PAGE_SIZE = 20
        const val FIRST_PAGE = 1
    }
}