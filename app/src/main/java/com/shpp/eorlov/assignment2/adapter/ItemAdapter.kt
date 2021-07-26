package com.shpp.eorlov.assignment2.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [PersonData] data object.
 */

class ItemAdapter(
    private val context: Context,
    private val dataset: MutableList<PersonData>,

    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val adapter: ItemAdapter = this

    /**
     * Class that represents an item of RecyclerView
     */
    class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val personNameTextView = binding.nameTextView
        val personProfessionTextView = binding.professionTextView
        val personImageImageView = binding.imageContactsImageView
        val clearButtonImageView = binding.clearButtonImageView
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
        holder.personNameTextView.text = context.resources.getString(item.userNameId)
        holder.personProfessionTextView.text =
            context.resources.getString(item.careerId)

        holder.personImageImageView.loadImageUsingGlide(context.resources.getString(item.photoId))

        holder.clearButtonImageView.setOnClickListener {
            removeItem(position)
        }
    }


    /**
     *  Removes item by clicking to button
     */
    private fun removeItem(position: Int) {
        val removedItem = dataset[position]
        dataset.removeAt(position)
        adapter.notifyItemRemoved(position)
        val snackbar = Snackbar.make(
            (context as Activity).findViewById(R.id.main_activity),
            "Contact has been removed",
            5000
        )

        snackbar.setAction("Cancel") {
            addItem(position, removedItem)
        }

        snackbar.show()
        notifyDataSetChanged()
    }

    private fun addItem(position: Int, removedItem: PersonData) {
        dataset.add(position, removedItem)
        notifyItemInserted(position);
    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

    var itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeItem(viewHolder.absoluteAdapterPosition)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }

}