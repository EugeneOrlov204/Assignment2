package com.shpp.eorlov.assignment2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import java.util.concurrent.TimeUnit


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserModel] data object.
 */

class ContactsRecyclerAdapter(
    private val contacts: List<UserModel> = ArrayList(),
    private val onContactRemoveListener: ContactRemoveListener
) : RecyclerView.Adapter<ContactsRecyclerAdapter.ItemViewHolder>() {


    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = contacts.size


    fun updateRecyclerData(newDataset: List<UserModel>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(MyDiffUtil(contacts, newDataset))
        (contacts as ArrayList).clear()
        contacts.addAll(newDataset)
        diffResult.dispatchUpdatesTo(this@ContactsRecyclerAdapter)
    }

    inner class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val personNameTextView = binding.textViewPersonName
        private val personProfessionTextView = binding.textViewPersonProfession
        private val personImageImageView = binding.imageViewPersonImage
        val clearButtonImageView = binding.imageViewRemoveButton

        fun bind() {
            with (contacts[bindingAdapterPosition]) {
                personNameTextView.text = username
                personProfessionTextView.text = career
                personImageImageView.loadImageUsingGlide(photo.toUri())
            }
            setListeners()
        }

        private fun setListeners() {
            RxView.clicks(itemView).throttleFirst(
                1000,
                TimeUnit.MILLISECONDS
            ).subscribe {
                onContactRemoveListener.onContactRemove(bindingAdapterPosition)
//                (itemView.context as MainActivity).removeItemFromRecyclerView(
//                    bindingAdapterPosition
//                )
            }
        }

    }


    class MyDiffUtil(
        private val oldList: List<UserModel>,
        private val newList: List<UserModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].email == newList[newItemPosition].email
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

