package com.example.githubuserapi.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserapi.R
import com.example.githubuserapi.views.fragment.DetailFollowFragment

class SectionPagerAdapter(private val mContext : Context, fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var user: String? = null

    private val tabTitles = arrayOf(
        R.string.followers,
        R.string.following)

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DetailFollowFragment.newInstance(user, "followers")
        } else {
            DetailFollowFragment.newInstance(user, "following")
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence = mContext.resources.getString(tabTitles[position])


}