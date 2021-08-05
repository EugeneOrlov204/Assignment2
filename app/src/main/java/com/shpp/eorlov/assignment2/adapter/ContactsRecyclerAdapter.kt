package com.shpp.eorlov.assignment2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.model.UserData
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.utils.MyDiffUtil
import com.shpp.eorlov.assignment2.viewholder.ItemViewHolder


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserData] data object.
 */

class ContactsRecyclerAdapter(
    private val items: ArrayList<UserData>
) : RecyclerView.Adapter<ItemViewHolder>() {


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
        holder.bind(items[position])
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = items.size


    fun updateRecyclerData(newDataset: ArrayList<UserData>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(MyDiffUtil(items, newDataset))
        items.toMutableList().clear()
        items.toMutableList().addAll(newDataset)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}

