package com.arkan.ridwan.storyapps.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.arkan.ridwan.storyapps.BuildConfig
import com.arkan.ridwan.storyapps.R
import com.arkan.ridwan.storyapps.databinding.FragmentDetailBinding
import com.arkan.ridwan.storyapps.model.ListStoryItem
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(
            androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar,
            true
        );
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        //mengganti theme
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        val name = arguments?.get("name").toString()
        val date = arguments?.get("date").toString()
        val description = arguments?.get("description").toString()
        val image = arguments?.get("image").toString()
        val id = arguments?.get("id").toString()

        val datalist = ListStoryItem(image, date, name, description, id)
        setDataView(datalist)

    }

    private fun setDataView(storyData: ListStoryItem) {
        binding.apply {
            binding.tvName.text = storyData.name
            binding.tvDate.text = storyData.createdAt
            binding.tvDescription.text = storyData.description
            binding.tvId.text = storyData.id
            Glide.with(requireContext()).load(storyData.photoUrl).into(imgPicture)
        }
    }

    companion object {
        const val STORY_DATA = "storyData"
    }
}