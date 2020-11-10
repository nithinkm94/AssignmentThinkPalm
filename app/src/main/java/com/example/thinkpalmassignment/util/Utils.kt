package com.example.thinkpalmassignment.util

import android.content.Context
import java.io.IOException


fun getJsonFromAssets(context: Context, fileName: String?): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName.toString()).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}


