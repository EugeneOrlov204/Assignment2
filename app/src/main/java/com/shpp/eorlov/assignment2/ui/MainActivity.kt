package com.shpp.eorlov.assignment2.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment2.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.recyclerview.ContactRemoveListener
import com.shpp.eorlov.assignment2.recyclerview.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.recyclerview.RecyclerViewModel
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.JSONHelper
import com.shpp.eorlov.assignment2.utils.JSONHelper.exportToJSON
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // view binding for the activity
    val recyclerViewModel: RecyclerViewModel by inject()

    lateinit var contactsRecyclerAdapter: ContactsRecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: ContactDialogFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val bundle = bundleOf("some_int" to 0)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()
        setObserver()
        setListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        exportToJSON(this, recyclerViewModel.userListLiveData.value ?: emptyList())
    }

    // after screen rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        recyclerViewModel.userListLiveData.value = JSONHelper.importFromJSON(this).toMutableList()
    }

    /**
     * Removes item on given position from RecyclerView
     */
    fun removeItemFromRecyclerView(
        position: Int,
    ) {

        val removedItem: UserModel = recyclerViewModel.getItem(position) ?: return
        recyclerViewModel.removeItem(position)

        Snackbar.make(
            binding.root,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            recyclerViewModel.addItem(position, removedItem)
        }.show()
    }

    private fun initRecycler() {
        recyclerViewModel.getPersonData()
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
            adapter =
                ContactsRecyclerAdapter(onContactRemoveListener = object : ContactRemoveListener {
                    override fun onContactRemove(position: Int) {
                        removeItemFromRecyclerView(position)
                    }
                })

            contactsRecyclerAdapter = binding.recyclerView.adapter as ContactsRecyclerAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }
    }

    private fun setObserver() {
        recyclerViewModel.userListLiveData.observe(this) { listPersonData ->
            (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(
                listPersonData
            )
        }

        recyclerViewModel.errorEvent.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        RxView.clicks(binding.textViewAddContacts).throttleFirst(
            500,
            TimeUnit.MILLISECONDS
        )
            .subscribe {
                dialog = ContactDialogFragment()
                dialog.show(supportFragmentManager, "contact")
            }
    }
}
