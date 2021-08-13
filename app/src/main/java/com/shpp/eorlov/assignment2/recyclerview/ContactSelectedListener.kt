package com.shpp.eorlov.assignment2.recyclerview

import android.net.Uri

interface ContactSelectedListener {
    fun onContactSelected(holder: ContactsRecyclerAdapter.ContactViewHolder, imagePath: Uri)
}