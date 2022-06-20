package com.inc.vr.corps.storymaps.view.ui.islogin.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentDetailBinding
import com.inc.vr.corps.storymaps.databinding.FragmentProfileBinding
import com.inc.vr.corps.storymaps.model.ListStoryItem
import com.inc.vr.corps.storymaps.view.ui.nologin.NoLoginActivity
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class ProfileFragment :  Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
            //mengganti theme
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        userViewModel.getUserPreferences().observe(requireActivity()) {
            binding.tvNama.text = it.name
        }
        binding.btnLogout.setOnClickListener {
            userViewModel.clearUserPreference()
            val intent = Intent(requireContext(), NoLoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}