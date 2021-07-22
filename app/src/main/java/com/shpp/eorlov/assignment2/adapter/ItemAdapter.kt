package com.shpp.eorlov.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.model.PersonData
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingPicasso


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [PersonData] data object.
 */

class ItemAdapter(
    private val context: Context,
    private val dataset: List<PersonData>

) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val personNameTextView: AppCompatTextView = binding.nameTextView
        val personProfessionTextView: AppCompatTextView =
            binding.professionTextView
        val personImage: AppCompatImageView =
            binding.imageContactsImageView
    }

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

        val item = dataset[position]
        holder.personNameTextView.text = context.resources.getString(item.nameResourceId)
        holder.personProfessionTextView.text =
            context.resources.getString(item.professionResourceId)

        holder.personImage.loadImageUsingPicasso("https://source.unsplash.com/random")

    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

}