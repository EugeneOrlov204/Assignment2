package com.shpp.eorlov.assignment2.ui.mainfragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.FragmentContentBinding
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.ui.SharedViewModel
import com.shpp.eorlov.assignment2.ui.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment2.ui.mainfragment.adapter.ContactClickListener
import com.shpp.eorlov.assignment2.ui.mainfragment.adapter.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.Constants.BUTTON_CLICK_DELAY
import com.shpp.eorlov.assignment2.utils.Constants.CONTACT_DIALOG_TAG
import com.shpp.eorlov.assignment2.utils.Constants.DIALOG_FRAGMENT_REQUEST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.LIST_OF_CONTACTS_KEY
import com.shpp.eorlov.assignment2.utils.Constants.NEW_CONTACT_KEY
import com.shpp.eorlov.assignment2.utils.ext.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import kotlin.math.abs


class MainFragment : Fragment(R.layout.fragment_content), ContactClickListener {

    // view binding for the activity
    private val viewModel: MainFragmentViewModel by inject()
    private val sharedViewModel: SharedViewModel by inject()
    private val contactsRecyclerAdapter: ContactsRecyclerAdapter by lazy {
        ContactsRecyclerAdapter(
            this
        )
    }
    private lateinit var binding: FragmentContentBinding
    private lateinit var dialog: ContactDialogFragment

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)

        initRecycler()
        setObservers()
        setListeners()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentState = viewModel.userListLiveData.value ?: emptyList()
        outState.putParcelableArray(
            LIST_OF_CONTACTS_KEY,
            currentState.toTypedArray()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val receivedState =
                savedInstanceState.getParcelableArray(LIST_OF_CONTACTS_KEY) ?: return

            val receivedList = mutableListOf<UserModel>()
            for (element in receivedState) {
                receivedList.add(element as UserModel)
            }

            viewModel.userListLiveData.value = receivedList
        }
    }

    override fun onContactRemove(position: Int) {
        removeItemFromRecyclerView(position)
    }

    override fun onContactSelected(contact: UserModel) {
        sharedElementTransitionWithSelectedContact(contact)
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
            getString(R.string.removed_contact_message),
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            viewModel.addItem(position, removedItem)
        }.show()
    }

    private fun initRecycler() {
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
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = contactsRecyclerAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }
    }

    private fun sharedElementTransitionWithSelectedContact(
        contact: UserModel,
    ) {

        val action = MainFragmentDirections.actionMainFragmentToDetailViewFragment(
            contact
        )
        findNavController().navigate(action)
    }


    private fun setObservers() {

        postponeEnterTransition()

        viewModel.userListLiveData.observe(viewLifecycleOwner) { list ->
            contactsRecyclerAdapter.updateRecyclerData(list.toList())


            // Start the transition once all views have been
            // measured and laid out
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }


        viewModel.errorEvent.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        sharedViewModel.newUser.observe(viewLifecycleOwner) { newUser ->
            newUser?.let {
                viewModel.addItem(newUser)
                sharedViewModel.newUser.value = null
            }
        }
    }

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    @ExperimentalCoroutinesApi
    private fun setListeners() {
        binding.textViewAddContacts.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > BUTTON_CLICK_DELAY) {
                    dialog = ContactDialogFragment()
                    dialog.show(childFragmentManager, CONTACT_DIALOG_TAG)

                    dialog.setFragmentResultListener(DIALOG_FRAGMENT_REQUEST_KEY) { key, bundle ->
                        if (key == DIALOG_FRAGMENT_REQUEST_KEY) {
                            viewModel.addItem(
                                bundle.getParcelable(NEW_CONTACT_KEY)
                                    ?: return@setFragmentResultListener
                            )
                        }
                    }

                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)
    }
}