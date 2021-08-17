package com.shpp.eorlov.assignment2.utils.ext

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.shpp.eorlov.assignment2.utils.CircleTransform
import com.squareup.picasso.Picasso

enum class LIBRARIES {
    GLIDE, PICASSO
}

private val DEFAULT_LIBRARY = LIBRARIES.GLIDE

fun AppCompatImageView.loadImage(url: String) {
    when (DEFAULT_LIBRARY) {

        LIBRARIES.GLIDE -> {
            Glide.with(this)
                .load(url)
                .circleCrop()
                .into(this)
        }

        LIBRARIES.PICASSO -> {
            Picasso.get()
                .load(url)
                .transform(CircleTransform())
                .into(this)
        }
    }
}

fun AppCompatImageView.loadImage(resource: Uri) {
    when (DEFAULT_LIBRARY) {

        LIBRARIES.GLIDE -> {
            Glide.with(this)
                .load(resource)
                .circleCrop()
                .into(this)
        }

        LIBRARIES.PICASSO -> {
            Picasso.get()
                .load(resource)
                .transform(CircleTransform())
                .into(this)
        }
    }
}

fun AppCompatImageView.loadImage(resource: Int) {
    when (DEFAULT_LIBRARY) {

        LIBRARIES.GLIDE -> {
            Glide.with(this)
                .load(resource)
                .circleCrop()
                .into(this)
        }

        LIBRARIES.PICASSO -> {
            Picasso.get()
                .load(resource)
                .transform(CircleTransform())
                .into(this)
        }
    }
}
