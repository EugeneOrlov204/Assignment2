package com.shpp.eorlov.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.model.PersonData
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingGlide
import com.shpp.eorlov.assignment2.utils.ext.loadImageUsingPicasso


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [PersonData] data object.
 */

//FIXME replace findViewById to View Binding
class ItemAdapter(
    private val context: Context,
    private val dataset: List<PersonData>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personNameTextView: AppCompatTextView = view.findViewById(R.id.name_text_view)
        val personProfessionTextView: AppCompatTextView =
            view.findViewById(R.id.profession_text_view)
        val personImage: AppCompatImageView =
            view.findViewById(R.id.image_contacts_image_view)
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: ConstraintLayout = view.findViewById(R.id.header_constraint_layout)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_ITEM) {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return ItemViewHolder(adapterLayout)
        } else if (viewType == TYPE_HEADER) {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.header, parent, false)
            return HeaderViewHolder(adapterLayout)
        }

        throw  RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = dataset[position - 1]
            holder.personNameTextView.text = context.resources.getString(item.nameResourceId)
            holder.personProfessionTextView.text =
                context.resources.getString(item.professionResourceId)

            holder.personImage.loadImageUsingPicasso("https://source.unsplash.com/random")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isPositionHeader(position) -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size + 1

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }
}