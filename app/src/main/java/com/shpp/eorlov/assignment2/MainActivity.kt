package com.shpp.eorlov.assignment2

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.adapter.ItemAdapter
import com.shpp.eorlov.assignment2.data.PersonData
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialog.ContactDialogFragment
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModel()
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var dialog: ContactDialogFragment
    private lateinit var settings: SharedPreferences
    private lateinit var adapterClickListener: ItemAdapter.AdapterClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeData()
        setContentView(binding.root)

    }

    fun removeItemFromViewModel(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        adapterClickListener = object: ItemAdapter.AdapterClickListener {
            override fun removeItem(item: PersonData) {
                adapterClickListener.removeItem(item)
            }
        }

        val removedItem: PersonData = model.getItem(position) ?: return
        model.removeItem(position)

        Snackbar.make(
            viewHolder.itemView,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            model.addItem(position, removedItem)
            itemAdapter.updateRecyclerData(model.dataset.value ?: return@setAction)
        }.show()

        itemAdapter.updateRecyclerData(model.dataset.value ?: return)
    }

    fun addContact(view: View) {
        val imageData = settings.getString(Constants.PREF_NAME, null)
        val newContact = dialog.addContact() ?: return
        model.addItem(
            newContact.username,
            newContact.career,
            imageData ?: "https://i.pravatar.cc/",
            newContact.residenceAddress,
            newContact.birthDate,
            newContact.phoneNumber,
            newContact.email

        )
        itemAdapter.updateRecyclerData(model.dataset.value?: return)
        settings.edit().clear().apply()
    }

    fun closeDialog(view: View) {
        dialog.dismiss()
        settings.edit().clear().apply()
    }

    private fun initializeData() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        model.getPersonData()
        itemAdapter = ItemAdapter(model.dataset.value ?: return)

        val recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = itemAdapter
            setHasFixedSize(true)
        }

        //Implement swipe-to-delete
        ItemTouchHelper(itemAdapter.itemTouchHelperCallBack).attachToRecyclerView(recyclerView)

        settings = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE)

        setObserver()
        setListeners()
    }

    private fun setObserver() {
        model.dataset.observe(this) { listPersonData ->
            itemAdapter.updateRecyclerData(listPersonData)
        }

        model.errorEvent.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        RxView.clicks(binding.mainActivity.rootView).throttleFirst(
            2000,
            TimeUnit.MILLISECONDS
        )
            .subscribe { empty ->
                dialog = ContactDialogFragment()
                dialog.show(supportFragmentManager, "contact")
            }
    }
}
