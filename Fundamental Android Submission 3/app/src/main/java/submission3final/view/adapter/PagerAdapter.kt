package submission3final.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import submission3final.view.fragments.FollowerFragment
import submission3final.view.fragments.FollowingFragment

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }
}