package com.shpp.eorlov.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.MainActivity
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ListItemBinding
import com.shpp.eorlov.assignment2.utils.MyDiffUtil
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [PersonData] data object.
 */

class ItemAdapter(
    private val context: Context,
    private var dataset: MutableList<PersonData>,

    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    /* Variable that implements swipe-to-delete */
    var itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (context as MainActivity).removeItemFromViewModel(
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

        with(holder) {
            personNameTextView.text = item.username
            personProfessionTextView.text = item.career
            personImageImageView.loadImageUsingGlide((item as PersonData) .photo)

            clearButtonImageView.setOnClickListener {
//            clearButtonImageView.isEnabled = false
                (context as MainActivity).removeItemFromViewModel(
                    holder,
                    absoluteAdapterPosition
                )
//            clearButtonImageView.isEnabled = true
            }
        }

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size


    fun updateItems(newDataset: List<PersonData>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(MyDiffUtil(dataset, newDataset))
        diffResult.dispatchUpdatesTo(this)
        dataset.clear()
        dataset.addAll(newDataset)
    }
}
