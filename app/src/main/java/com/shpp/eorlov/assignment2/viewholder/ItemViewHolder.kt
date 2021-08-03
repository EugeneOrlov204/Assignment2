
package com.shpp.eorlov.assignment2.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.databinding.ListItemBinding

/**
 * Class that represents an item of RecyclerView
 */
class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val personNameTextView = binding.textViewPersonName
    val personProfessionTextView = binding.textViewPersonProfession
    val personImageImageView = binding.imageViewPersonImage
    val clearButtonImageView = binding.imageViewClearButton
}