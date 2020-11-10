package com.example.thinkpalmassignment.data

import com.google.gson.annotations.SerializedName

data class Items (
    @SerializedName("name") val name : String,
    @SerializedName("poster-image") val image : String
)