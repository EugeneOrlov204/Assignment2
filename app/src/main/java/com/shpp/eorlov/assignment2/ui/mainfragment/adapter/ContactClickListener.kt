package com.shpp.eorlov.assignment2.ui.mainfragment.adapter

import com.shpp.eorlov.assignment2.model.UserModel

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactSelected(contact: UserModel)
}