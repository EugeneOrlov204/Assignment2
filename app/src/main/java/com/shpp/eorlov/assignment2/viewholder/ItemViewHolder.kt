
package com.shpp.eorlov.assignment2.viewholder

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import java.util.concurrent.TimeUnit

/**
 * Class that represents an item of RecyclerView
 */
class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val personNameTextView = binding.textViewPersonName
    val personProfessionTextView = binding.textViewPersonProfession
    val personImageImageView = binding.imageViewPersonImage
    val clearButtonImageView = binding.imageViewClearButton

    fun bind(item: PersonData) {
        personNameTextView.text = item.username
        personProfessionTextView.text = item.career
        personImageImageView.loadImageUsingGlide(item.photo.toUri())
        setListeners()
    }

    private fun setListeners() {
        RxView.clicks(itemView).throttleFirst(
            500,
            TimeUnit.MILLISECONDS
        ).subscribe {
            (itemView.context as MainActivity).removeItemFromViewModel(
                this,
                absoluteAdapterPosition
            )
        }
    }

}