package com.shpp.eorlov.assignment2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.ui.MainActivity
import com.shpp.eorlov.assignment2.utils.MyDiffUtil
import com.shpp.eorlov.assignment2.utils.ext.loadImage
import java.util.concurrent.TimeUnit


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserModel] data object.
 */

class ContactsRecyclerAdapter(
    private val contacts: List<UserModel> = ArrayList(),
    private val onContactRemoveListener: ContactRemoveListener,
    private val onContactSelectedListener: ContactSelectedListener
) : RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder>() {


    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
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

    inner class ContactViewHolder(binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val textViewPersonName = binding.textViewPersonName
        private val textViewPersonProfession = binding.textViewPersonProfession
        private val imageViewPersonImage = binding.imageViewPersonImage
        private val imageViewRemoveButton = binding.imageViewRemoveButton
        private val constraintLayoutContact = binding.constraintLayoutContact

        fun bind() {
            with(contacts[bindingAdapterPosition]) {
                textViewPersonName.text = username
                textViewPersonProfession.text = career
                imageViewPersonImage.loadImage(photo.toUri())
            }
            setListeners()
        }

        private fun setListeners() {
            RxView.clicks(imageViewRemoveButton).throttleFirst(
                1000,
                TimeUnit.MILLISECONDS
            ).subscribe {
                onContactRemoveListener.onContactRemove(bindingAdapterPosition)
            }

            RxView.clicks(constraintLayoutContact).throttleFirst(
                1000,
                TimeUnit.MILLISECONDS
            ).subscribe {
                val args: MutableList<String> = mutableListOf()
                with(contacts[bindingAdapterPosition]) {
                    args.add(photo)
                    args.add(username)
                    args.add(career)
                    args.add(residenceAddress)
                }

                onContactSelectedListener.onContactSelected(args)
            }
        }
    }
}

