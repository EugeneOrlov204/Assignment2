package com.shpp.eorlov.assignment2.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment2.databinding.FragmentDetailViewBinding
import com.shpp.eorlov.assignment2.utils.ext.loadImage


class DetailViewFragment : Fragment() {
    private val args: DetailViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.imageButtonContactDialogCloseButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewUserImageDetailView.loadImage(args.contactPhotoUri.toUri())
        binding.textViewUserNameDetailView.text = args.contactName
        binding.textViewUserProfessionDetailView.text = args.contactCareer
        binding.textViewUserResidence.text = args.contactResidence
    }
}