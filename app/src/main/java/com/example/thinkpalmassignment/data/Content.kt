package com.example.thinkpalmassignment.data

import com.google.gson.annotations.SerializedName

data class Content (
    @SerializedName("content") val content : List<Items>)