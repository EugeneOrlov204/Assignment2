package com.shpp.eorlov.assignment2

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.adapter.ContactRemoveListener
import com.shpp.eorlov.assignment2.adapter.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: ContactDialogFragment
    lateinit var settings: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getString(Constants.USERS_STATE_KEY)?.let {
            // after screen rotation
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initRecycler() // todo
        initializeData()
        setObserver()
        setListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val json: String = "json"
        outState.putString(Constants.USERS_STATE_KEY, json)
        super.onSaveInstanceState(outState)
    }

    /**
     * Removes item on given position from RecyclerView
     */
    fun removeItemFromRecyclerView(
        position: Int,
    ) {

        val removedItem: UserModel = viewModel.getItem(position) ?: return
        viewModel.removeItem(position)

        Snackbar.make(
            binding.root,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            viewModel.addItem(position, removedItem)
            (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(viewModel.userListLiveData.value!!)
        }.show()

        (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(viewModel.userListLiveData.value!!)
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

        (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(viewModel.userListLiveData.value!!)
        settings.edit().clear().apply()
    }

    private fun initializeData() {
        viewModel.getPersonData()
        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    removeItemFromRecyclerView(
                        viewHolder.bindingAdapterPosition
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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = ContactsRecyclerAdapter(onContactRemoveListener = object: ContactRemoveListener {
                override fun onContactRemove(position: Int) {
                    removeItemFromRecyclerView(position)
                }
            })

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }



        settings = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE)
    }

    private fun setObserver() {
        viewModel.userListLiveData.observe(this) { listPersonData ->
            (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(listPersonData)
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
