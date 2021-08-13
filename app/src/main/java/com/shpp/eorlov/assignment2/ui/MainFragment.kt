package com.shpp.eorlov.assignment2.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.view.RxView
import com.shpp.eorlov.assignment2.R
import com.shpp.eorlov.assignment2.databinding.FragmentContentBinding
import com.shpp.eorlov.assignment2.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment2.model.UserModel
import com.shpp.eorlov.assignment2.recyclerview.ContactRemoveListener
import com.shpp.eorlov.assignment2.recyclerview.ContactSelectedListener
import com.shpp.eorlov.assignment2.recyclerview.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment2.recyclerview.FragmentViewModel
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.Constants.DATA_OF_LIST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.DIALOG_FRAGMENT_REQUEST_KEY
import com.shpp.eorlov.assignment2.utils.Constants.NEW_CONTACT_KEY
import com.shpp.eorlov.assignment2.utils.JSONHelper
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class MainFragment : Fragment(R.layout.fragment_content) {

    // view binding for the activity
    private val fragmentViewModel: FragmentViewModel by inject()

    lateinit var contactsRecyclerAdapter: ContactsRecyclerAdapter
    private lateinit var binding: FragmentContentBinding
    private lateinit var dialog: ContactDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)

        initRecycler()
        setObserver()
        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val jsonString =
            JSONHelper.exportToJSON(fragmentViewModel.userListLiveData.value ?: emptyList())
        outState.putString(DATA_OF_LIST_KEY, jsonString)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            fragmentViewModel.userListLiveData.value = JSONHelper.importFromJSON(
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

        val removedItem: UserModel = fragmentViewModel.getItem(position) ?: return
        fragmentViewModel.removeItem(position)

        Snackbar.make(
            binding.root,
            "Contact has been removed",
            Constants.SNACKBAR_DURATION
        ).setAction("Cancel") {
            fragmentViewModel.addItem(position, removedItem)
        }.show()
    }

    private fun initRecycler() {
        fragmentViewModel.getPersonData()
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
            adapter =
                ContactsRecyclerAdapter(onContactRemoveListener = object : ContactRemoveListener {
                    override fun onContactRemove(position: Int) {
                        removeItemFromRecyclerView(position)
                    }
                },
                    onContactSelectedListener = object : ContactSelectedListener {
                        override fun onContactSelected(
                            holder: ContactsRecyclerAdapter.ContactViewHolder,
                            imagePath: Uri
                        ) {
                            sharedElementTransitionWithSelectedContact(holder, imagePath)
                        }
                    })

            contactsRecyclerAdapter = binding.recyclerView.adapter as ContactsRecyclerAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }
    }

    private fun sharedElementTransitionWithSelectedContact(
        holder: ContactsRecyclerAdapter.ContactViewHolder,
        imagePath: Uri
    ) {

        val action = ArtistsFragmentDirections.navToArtistDetail(uri = artist.imageUri)
        findNavController().navigate(action, extras)

        val extras = FragmentNavigatorExtras(
            holder.imageViewPersonImage to "contactPhoto",
            holder.textViewPersonResidence to "contactResidence",
            holder.textViewPersonName to "contactName",
            holder.textViewPersonProfession to "contactProfession"
        )

        findNavController().navigate(
            R.id.action_mainFragment_to_detailViewFragment,
            null,
            null,
            extras
        )

    }

    private fun setObserver() {

        postponeEnterTransition()

        fragmentViewModel.userListLiveData.observe(viewLifecycleOwner) { listPersonData ->
            (binding.recyclerView.adapter as ContactsRecyclerAdapter).updateRecyclerData(
                listPersonData
            )

            // Start the transition once all views have been
            // measured and laid out
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }

        fragmentViewModel.errorEvent.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        RxView.clicks(binding.textViewAddContacts).throttleFirst(
            500,
            TimeUnit.MILLISECONDS
        )
            .subscribe {
                dialog = ContactDialogFragment()
                dialog.show(childFragmentManager, "contact_dialog")
                dialog.setFragmentResultListener(DIALOG_FRAGMENT_REQUEST_KEY) { key, bundle ->
                    if (key == DIALOG_FRAGMENT_REQUEST_KEY) {
                        fragmentViewModel.addItem(
                            JSONHelper.importFromJSON(bundle.getString(NEW_CONTACT_KEY))[0]
                        )
                    }
                }
            }
    }
}