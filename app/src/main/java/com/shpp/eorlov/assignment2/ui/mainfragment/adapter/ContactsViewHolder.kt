package com.shpp.eorlov.assignment2.ui.mainfragment.adapter

import android.os.SystemClock
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.ext.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs

class ContactsViewHolder(
    private val binding: ListItemBinding,
    private val onContactClickListener: ContactClickListener,
    private val contacts: List<UserModel>,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.ViewHolder(binding.root) {

    @ExperimentalCoroutinesApi
    fun bind() {
        binding.apply {
            contacts[absoluteAdapterPosition].apply {
                textViewPersonName.text = name
                textViewPersonProfession.text = profession
                draweeViewPersonImage.setImageURI(photo)
            }
        }
        setListeners()
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()

    @ExperimentalCoroutinesApi
    private fun setListeners() {
        binding.imageViewRemoveButton.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    onContactClickListener.onContactRemove(bindingAdapterPosition)
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)


        binding.constraintLayoutContact.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    val contact = contacts[bindingAdapterPosition]
                    onContactClickListener.onContactSelected(contact)
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)

    }
}