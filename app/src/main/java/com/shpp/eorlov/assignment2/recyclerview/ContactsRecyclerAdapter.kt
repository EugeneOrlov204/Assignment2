package com.shpp.eorlov.assignment2.recyclerview

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.ui.DetailViewFragment
import com.shpp.eorlov.assignment2.ui.MainActivity
import com.shpp.eorlov.assignment2.utils.MyDiffUtil
import com.shpp.eorlov.assignment2.utils.ext.loadImage
import java.util.concurrent.TimeUnit


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserModel] data object.
 */

class ContactsRecyclerAdapter(
    private val contacts: List<UserModel> = ArrayList(),
    private val onContactRemoveListener: ContactRemoveListener
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
        private val textViewPersonResidence = binding.textViewPersonResidence

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
//                Navigation
//                    .findNavController(constraintLayoutContact)
//                    .navigate(R.id.action_mainFragment_to_detailViewFragment)

                sharedElementTransition()
            }
        }

//        private fun sharedElementTransition() {
////            val pair: android.util.Pair<View, String>
////            pair.add(Pair(textViewPersonName, "contactName"))
////            pair.add(Pair(textViewPersonProfession, "contactProfession"))
////            pair.add(Pair(imageViewPersonImage, "contactPhoto"))
//////            pair.add(Pair(textViewPersonName, "contactName"))
//
//            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
//                itemView.context as MainActivity,
//                android.util.Pair(textViewPersonName, "contactName"),
//                android.util.Pair(textViewPersonProfession, "contactProfession"),
//                android.util.Pair(imageViewPersonImage, "contactPhoto")
//            )
//            val intent = Intent(itemView.context as MainActivity, DetailViewFragment::class.java)
//            val nextFrag = DetailViewFragment()
//            getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.Layout_container, nextFrag, "findThisFragment")
//                .addToBackStack(null)
//                .commit()
//        }

        private fun sharedElementTransition() {

            (itemView.context as MainActivity).supportFragmentManager
                .beginTransaction()
                .addSharedElement(textViewPersonName, "contactName")
                .addSharedElement(textViewPersonProfession, "contactProfession")
                .addSharedElement(imageViewPersonImage, "contactPhoto")
                .addSharedElement(textViewPersonResidence, "contactResidence")
                .replace(R.id.constraintLayoutMainFragment, DetailViewFragment())
                .addToBackStack(null)
                .commit();
        }
    }
}

