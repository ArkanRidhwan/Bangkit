package com.inc.vr.corps.storymaps.view.ui.islogin.detail

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentDetailBinding
import com.inc.vr.corps.storymaps.model.ListStoryItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class DetailFragment :  Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
            //mengganti theme
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        //name, date, description, image,id
        val name = arguments?.get("name") .toString()
        val date = arguments?.get("date").toString()
        val description = arguments?.get("description").toString()
        val image = arguments?.get("image").toString()
        val id = arguments?.get("id").toString()

        val datalist = ListStoryItem(image, date, name,description, id)
        setDataView(datalist)
        //add data to ListStoryItem
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