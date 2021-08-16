package com.shpp.eorlov.assignment2.ui.details

import android.os.Bundle
import android.os.SystemClock
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment2.databinding.FragmentDetailViewBinding
import com.shpp.eorlov.assignment2.utils.Constants
import com.shpp.eorlov.assignment2.utils.ext.clicks
import com.shpp.eorlov.assignment2.utils.ext.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs


class DetailViewFragment : Fragment() {

    private val args: DetailViewFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    @ExperimentalCoroutinesApi
    private fun setListeners() {

        binding.imageButtonContactDialogCloseButton.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    activity?.onBackPressed()
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initViews() {
        args.contact.apply {
            binding.imageViewUserImageDetailView.loadImage(photo)
            binding.textViewUserNameDetailView.text = name
            binding.textViewUserProfessionDetailView.text = profession
            binding.textViewUserResidence.text = residenceAddress
        }
    }
}
