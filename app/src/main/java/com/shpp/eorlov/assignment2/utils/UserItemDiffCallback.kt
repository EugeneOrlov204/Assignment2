package com.shpp.eorlov.assignment2.utils

import androidx.recyclerview.widget.DiffUtil
import com.shpp.eorlov.assignment2.model.UserModel

class UserItemDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem == newItem
}