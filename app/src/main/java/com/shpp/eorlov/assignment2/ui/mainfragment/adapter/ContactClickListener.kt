package com.shpp.eorlov.assignment2.ui.mainfragment.adapter

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactSelected(args: MutableList<String>)
}