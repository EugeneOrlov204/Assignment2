package com.shpp.eorlov.assignment2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialog.ContactDialogFragment
import com.shpp.eorlov.assignment2.model.ViewModelForRecyclerView
import com.shpp.eorlov.assignment2.utils.Constants


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var modelViewModel: ViewModelForRecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var dialog: ContactDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Add listener to add contact button, that create DialogFragment
        val addContactsButton = binding.addContactsTextView
        addContactsButton.setOnClickListener {
            addContactsButton.isEnabled = false
            dialog = ContactDialogFragment()
            dialog.show(supportFragmentManager, "contact")
        }

        modelViewModel = ViewModelProvider(this)
            .get(ViewModelForRecyclerView::class.java)

        //modelViewModel.getPersonData().toMutableList() means that we take copy of given data
        itemAdapter = ItemAdapter(this, modelViewModel.getPersonData().toMutableList())

        val recyclerView = binding.recyclerView
        recyclerView.adapter = itemAdapter
        recyclerView.setHasFixedSize(true)

        //Implement swipe-to-delete
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
            itemAdapter.updateItems(modelViewModel.getPersonData())
        }

        snackbar.show()
        itemAdapter.updateItems(modelViewModel.getPersonData())
    }

    fun addContact(view: View) {
        dialog.addContact(modelViewModel)
        itemAdapter.updateItems(modelViewModel.getPersonData())
    }

    fun closeDialog(view: View) {
        dialog.dismiss()
    }
}
