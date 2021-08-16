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
import com.shpp.eorlov.assignment2.utils.Constants.DATA_OF_LIST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.DIALOG_FRAGMENT_REQUEST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.NEW_CONTACT_KEY
import com.shpp.eorlov.assignment2.utils.JSONHelper
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

        setObservers()
        initRecycler()
        setListeners()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val jsonString =
            JSONHelper.exportToJSON(viewModel.userListLiveData.value ?: emptyList())
        outState.putString(DATA_OF_LIST_KEY, jsonString)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.userListLiveData.value = JSONHelper.importFromJSON(
                savedInstanceState.getString(DATA_OF_LIST_KEY)
            ).toMutableList()
        }
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
        args: MutableList<String>,
    ) {

        val action = MainFragmentDirections.actionMainFragmentToDetailViewFragment(
            contactPhotoUri = args[0],
            contactName = args[1],
            contactCareer = args[2],
            contactResidence = args[3]
        )
        findNavController().navigate(action)
    }


    private fun setObservers() {

        postponeEnterTransition()

        viewModel.userListLiveData.observe(viewLifecycleOwner, { list ->
            contactsRecyclerAdapter.updateRecyclerData(list)

            // Start the transition once all views have been
            // measured and laid out
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })


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
                if(abs(SystemClock.uptimeMillis() - previousClickTimestamp) > BUTTON_CLICK_DELAY) {
                    dialog = ContactDialogFragment()
                    dialog.show(childFragmentManager, "contact_dialog")
                    dialog.setFragmentResultListener(DIALOG_FRAGMENT_REQUEST_KEY) { key, bundle ->
                        if (key == DIALOG_FRAGMENT_REQUEST_KEY) {
                            viewModel.addItem(
                                JSONHelper.importFromJSON(bundle.getString(NEW_CONTACT_KEY))[0]
                            )
                        }
                    }
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onContactRemove(position: Int) {
        removeItemFromRecyclerView(position)
    }

    override fun onContactSelected(args: MutableList<String>) {
        sharedElementTransitionWithSelectedContact(args)
    }
}