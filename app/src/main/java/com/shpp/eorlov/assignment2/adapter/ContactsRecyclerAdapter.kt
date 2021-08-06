package com.shpp.eorlov.assignment2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.model.UserData
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.viewholder.ItemViewHolder


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserData] data object.
 */

class ContactsRecyclerAdapter(
    private val items: List<UserData>
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


    fun updateRecyclerData(newDataset: List<UserData>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(MyDiffUtil(items, newDataset))
        diffResult.dispatchUpdatesTo(this)
        items.toMutableList().clear()
        items.toMutableList().addAll(newDataset)
        notifyDataSetChanged()
    }


    class MyDiffUtil(
        private val oldList: List<UserData>,
        private val newList: List<UserData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].username == newList[newItemPosition].username
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldList[oldItemPosition].username != newList[newItemPosition].username -> false

                else -> true
            }
        }
    }
}

