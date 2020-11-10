package com.example.thinkpalmassignment.data

import com.google.gson.annotations.SerializedName

data class Page (
	@SerializedName("title") val title : String,
	@SerializedName("total-content-items") val total_content_items : Int,
	@SerializedName("page-num") val page_num : Int,
	@SerializedName("page-size") val page_size : Int,
	@SerializedName("content-items")val content_items : Content
)