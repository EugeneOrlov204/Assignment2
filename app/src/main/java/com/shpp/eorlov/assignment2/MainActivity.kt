package com.shpp.eorlov.assignment2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.model.ViewModelForRecyclerView
import com.shpp.eorlov.assignment2.utils.Constants


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var modelViewModel: ViewModelForRecyclerView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        modelViewModel = ViewModelProvider(this)
            .get(ViewModelForRecyclerView::class.java)


        itemAdapter = ItemAdapter(this, modelViewModel.getPersonData().toMutableList())

        val recyclerView = binding.recyclerView
        recyclerView.adapter = itemAdapter

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)

        ItemTouchHelper(itemAdapter.itemTouchHelperCallBack).attachToRecyclerView(recyclerView)

        setContentView(binding.root)
    }

    fun removeItemFromViewModel(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
    ) {

        val removedItem: PersonData = modelViewModel.getItem(position)
        modelViewModel.removeItem(position)

        val snackbar = Snackbar.make(
            viewHolder.itemView,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        )

        snackbar.setAction("Cancel") {
            modelViewModel.addItem(position, removedItem)
            this.itemAdapter.updateItems(modelViewModel.getPersonData())
        }

        snackbar.show()
        this.itemAdapter.updateItems(modelViewModel.getPersonData())
    }
}
