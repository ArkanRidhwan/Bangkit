package submission3final.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission3final.githubuserapp.databinding.FragmentFollowingBinding
import submission3final.viewmodel.FollowingViewModel
import submission3final.view.DetailUserActivity
import submission3final.view.adapter.FollowAdapter
import submission3final.database.FavoriteDb
import submission3final.model.response.UsersResponse

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intentUser =
            activity?.intent?.getParcelableExtra(DetailUserActivity.EXTRA_USER) as UsersResponse?
        val intentFav =
            activity?.intent?.getParcelableExtra(DetailUserActivity.EXTRA_FAV) as FavoriteDb?

        val username = if (intentUser != null) {
            intentUser.login
        } else {
            intentFav?.username
        }

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]

        if (username != null) {
            initData(username)
        }
    }

    private fun initData(username: String) {
        showLoading(true)
        followingViewModel.setFowllowing(username, requireContext())
        followingViewModel.getFollowing().observe(requireActivity()) { userResp ->
            if (userResp!!.isNotEmpty()) {
                showLoading(false)
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.rvFollowing.layoutManager = GridLayoutManager(context, 2)
                } else {
                    binding.rvFollowing.layoutManager = LinearLayoutManager(context)
                }


                val followAdapter = FollowAdapter(requireContext(), userResp)
                binding.rvFollowing.adapter = followAdapter


            } else {
                showLoading(false)
                binding.tvNoneFollowing.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}