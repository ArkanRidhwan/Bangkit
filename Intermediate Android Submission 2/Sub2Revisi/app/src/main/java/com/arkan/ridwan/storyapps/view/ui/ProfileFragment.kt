package com.arkan.ridwan.storyapps.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arkan.ridwan.storyapps.BuildConfig
import com.arkan.ridwan.storyapps.R
import com.arkan.ridwan.storyapps.databinding.FragmentProfileBinding
import com.arkan.ridwan.storyapps.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by viewModels<UserVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(
            androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar,
            true
        );
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        userViewModel.getUserPreferences().observe(requireActivity()) {
            binding.tvNama.text = "Hi " + it.name
        }
        binding.btnLogout.setOnClickListener {
            userViewModel.clearUserPreference()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}