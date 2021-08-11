package com.shpp.eorlov.assignment2.utils

import com.google.gson.Gson
import com.shpp.eorlov.assignment2.model.UserModel

object JSONHelper {
    fun exportToJSON(dataList: List<UserModel>): String? {
        val gson = Gson()
        val dataItems = DataItems()
        dataItems.users = dataList
        return gson.toJson(dataItems)
    }

    fun importFromJSON(jsonString: String?): List<UserModel> {
        val gson = Gson()
        val dataItems = gson.fromJson(jsonString, DataItems::class.java)
        return dataItems.users
    }

    private class DataItems {
        var users: List<UserModel> = emptyList()
    }
}