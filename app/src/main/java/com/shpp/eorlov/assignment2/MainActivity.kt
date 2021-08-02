package com.shpp.eorlov.assignment2

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialog.ContactDialogFragment
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import com.shpp.eorlov.assignment2.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private lateinit var binding: ActivityMainBinding
    private val modelViewModel: MainViewModel by viewModel()
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var dialog: ContactDialogFragment
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setListeners()

        //modelViewModel.getPersonData().toMutableList() means that we take copy of given data
        itemAdapter = ItemAdapter(this, modelViewModel.getPersonData().toMutableList())

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = itemAdapter
        recyclerView.setHasFixedSize(true)

        //Implement swipe-to-delete
        ItemTouchHelper(itemAdapter.itemTouchHelperCallBack).attachToRecyclerView(recyclerView)

        settings = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE)
        setContentView(binding.root)
    }

    /**
     * Add listener to add contact button, that create DialogFragment
     */
    private fun setListeners() {
        with(binding) {
            textViewAddContacts.setOnClickListener {
                dialog = ContactDialogFragment()
                dialog.show(supportFragmentManager, "contact")
            }
        }
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
        val imageData = settings.getString(Constants.PREF_NAME, null)
        val newContact = dialog.addContact() ?: return
        modelViewModel.addItem(
            newContact.username,
            newContact.career,
            imageData ?: "https://i.pravatar.cc/",
            newContact.residenceAddress,
            newContact.birthDate,
            newContact.phoneNumber,
            newContact.email

        )
        itemAdapter.updateItems(modelViewModel.getPersonData())
        settings.edit().clear().apply()
    }

    fun closeDialog(view: View) {
        dialog.dismiss()
        settings.edit().clear().apply()
    }
}
