package com.shpp.eorlov.assignment2.utils.ext

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.shpp.eorlov.assignment2.utils.CircleTransform
import com.squareup.picasso.Picasso

fun AppCompatImageView.loadImageUsingGlide(url: String) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .into(this)
}

fun AppCompatImageView.loadImageUsingGlide(resource: Int) {
    Glide.with(this)
        .load(resource)
        .circleCrop()
        .into(this)
}


fun AppCompatImageView.loadImageUsingPicasso(url: String) {
    Picasso.get()
        .load(url)
        .transform(CircleTransform())
        .into(this)
}

fun AppCompatImageView.loadImageUsingPicasso(resource: Int) {

    Picasso.get()
        .load(resource)
        .into(this)
}