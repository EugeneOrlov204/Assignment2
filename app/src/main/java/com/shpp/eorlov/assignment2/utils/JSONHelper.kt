package com.shpp.eorlov.assignment2.utils

import android.content.Context
import com.google.gson.Gson
import com.shpp.eorlov.assignment2.model.UserModel
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

object JSONHelper {
    private const val FILE_NAME = "recycler_data.json"
    fun exportToJSON(context: Context, dataList: List<UserModel>): Boolean {
        val gson = Gson()
        val dataItems = DataItems()
        dataItems.users = dataList
        val jsonString = gson.toJson(dataItems)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    fun importFromJSON(context: Context): List<UserModel> {
        var streamReader: InputStreamReader? = null
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(FILE_NAME)
            streamReader = InputStreamReader(fileInputStream)
            val gson = Gson()
            val dataItems = gson.fromJson(streamReader, DataItems::class.java)
            return dataItems.users
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return emptyList()
    }

    private class DataItems {
        var users: List<UserModel> = emptyList()
    }
}