package com.shpp.eorlov.assignment2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.utils.MyDiffUtil
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import com.shpp.eorlov.assignment2.viewholder.ItemViewHolder
import java.util.concurrent.TimeUnit


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [PersonData] data object.
 */

class ItemAdapter(dataset: MutableList<PersonData?>) : RecyclerView.Adapter<ItemViewHolder>() {
    var items: List<PersonData?> = dataset

    /* Variable that implements swipe-to-delete */
    var itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder.itemView.context as MainActivity).removeItemFromViewModel(
                    viewHolder,
                    viewHolder.absoluteAdapterPosition
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }


    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ItemViewHolder(binding)
        setListeners(viewHolder)
        return viewHolder
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        with(holder) {
            personNameTextView.text = item?.username
            personProfessionTextView.text = item?.career
            personImageImageView.loadImageUsingGlide(item?.photo?.toUri() ?: "")
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = items.size

    interface AdapterClickListener {
        fun removeItem(item: PersonData)
    }

    fun updateRecyclerData(newDataset: List<PersonData?>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(MyDiffUtil(items, newDataset))
        diffResult.dispatchUpdatesTo(this)
        items.toMutableList().clear()
        items.toMutableList().addAll(newDataset)
    }

    override fun removeItem(item: PersonData) {
        if(items.isNotEmpty()) {
            items.toMutableList().remove(item)
        }
    }

    private fun setListeners(holder: ItemViewHolder) {
        RxView.clicks(holder.itemView).throttleFirst(
            500,
            TimeUnit.MILLISECONDS
        ).subscribe { empty ->
            (holder.itemView.context as MainActivity).removeItemFromViewModel(
                holder,
                holder.absoluteAdapterPosition
            )
        }
    }
}

