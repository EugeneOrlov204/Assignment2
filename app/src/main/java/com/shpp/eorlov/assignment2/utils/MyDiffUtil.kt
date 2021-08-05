package com.shpp.eorlov.assignment2.utils

import androidx.recyclerview.widget.DiffUtil
import com.shpp.eorlov.assignment2.model.UserData

// pass two list one oldList and second newList
class MyDiffUtil(
    private val oldList: ArrayList<UserData>,
    private val newList: ArrayList<UserData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        // return oldList size
        return oldList.size
    }

    override fun getNewListSize(): Int {
        // return newList size
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // compare items based on their unique id
        return oldList[oldItemPosition].email == newList[newItemPosition].email
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // in here compare each item if they are same or different
        // return false if any data is same else return true
        return when {
            oldList[oldItemPosition].email != newList[newItemPosition].email -> false

            else -> true
        }
    }
}