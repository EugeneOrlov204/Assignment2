package com.shpp.eorlov.assignment2

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.adapter.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.model.UserData
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactsRecyclerAdapter: ContactsRecyclerAdapter
    private lateinit var dialog: ContactDialogFragment
    lateinit var settings: SharedPreferences

//    private lateinit var adapterClickListener: ItemAdapter.AdapterClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeData()
    }

    /**
     * Removes item on given position from RecyclerView
     */
    fun removeItemFromRecyclerView(
        position: Int,
    ) {
//        adapterClickListener = object: ItemAdapter.AdapterClickListener {
//            override fun removeItem(item: PersonData) {
//                adapterClickListener.removeItem(item)
//            }
//        }

        val removedItem: UserData = viewModel.getItem(position) ?: return
        viewModel.removeItem(position)

        Snackbar.make(
            binding.root,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            viewModel.addItem(position, removedItem)
            contactsRecyclerAdapter.updateRecyclerData(viewModel.userListLiveData.value!!)
//            itemAdapter.notifyDataSetChanged();
        }.show()

        contactsRecyclerAdapter.updateRecyclerData(viewModel.userListLiveData.value!!)
//        itemAdapter.notifyDataSetChanged();
    }

    fun addContact() {
        val imageData = settings.getString(Constants.PREF_NAME, null)
        val newContact = dialog.addContact() ?: return
        viewModel.addItem(
            newContact.username,
            newContact.career,
            imageData ?: "https://i.pravatar.cc/",
            newContact.residenceAddress,
            newContact.birthDate,
            newContact.phoneNumber,
            newContact.email
        )

        contactsRecyclerAdapter.updateRecyclerData(viewModel.userListLiveData.value!!)
        settings.edit().clear().apply()
    }

    private fun initializeData() {
        viewModel.getPersonData()

        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewHolder.itemView.context as MainActivity).removeItemFromRecyclerView(
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

        contactsRecyclerAdapter = ContactsRecyclerAdapter(viewModel.userListLiveData.value!!)

        val recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = contactsRecyclerAdapter
            setHasFixedSize(true)
        }

        //Implement swipe-to-delete
        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView)

        settings = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE)

        setObserver()
        setListeners()
    }

    private fun setObserver() {
        viewModel.userListLiveData.observe(this) { listPersonData ->
            contactsRecyclerAdapter.updateRecyclerData(listPersonData)
        }

        viewModel.errorEvent.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        RxView.clicks(binding.mainActivity.rootView).throttleFirst(
            2000,
            TimeUnit.MILLISECONDS
        )
            .subscribe {
                dialog = ContactDialogFragment()
                dialog.show(supportFragmentManager, "contact")
            }
    }
}
