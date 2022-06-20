package submission3final.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission3final.githubuserapp.R
import com.submission3final.githubuserapp.databinding.ActivityDetailUserBinding
import submission3final.view.adapter.PagerAdapter
import submission3final.database.FavoriteDb
import submission3final.model.response.UserDetailResponse
import submission3final.model.response.UsersResponse
import submission3final.viewmodel.DetailUserViewModel
import submission3final.viewmodel.FavUpdateViewModel
import submission3final.viewmodel.ViewModelFactory

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userDetailViewModel: DetailUserViewModel
    private lateinit var favUpdateViewModel: FavUpdateViewModel
    private lateinit var userData: UserDetailResponse

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = false


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAV = "extra_fav"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDetailViewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
        favUpdateViewModel = obtainViewModel(this@DetailUserActivity)


        val user = intent.getParcelableExtra(EXTRA_USER) as UsersResponse?
        val userFav = intent.getParcelableExtra(EXTRA_FAV) as FavoriteDb?

        binding.ivUnFav.isEnabled = false
        binding.ivFav.isEnabled = false

        if (user != null) {
            initData(user.login)
        } else {
            if (userFav?.favorite!!) {
                binding.ivFav.visibility = View.VISIBLE
                binding.ivUnFav.visibility = View.INVISIBLE
            }
            initData(userFav.username)
        }


        supportActionBar!!.title = user?.login

        val pagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fbAdd.setOnClickListener {
            onAddButtonClicked()
        }

        binding.fbShare.setOnClickListener {
            shareDetail(userData)

        }

        binding.fbReport.setOnClickListener {
            Toast.makeText(this, "Kamu telah melaporkan pengguna", Toast.LENGTH_SHORT).show()
        }

        binding.ivUnFav.setOnClickListener {
            if (binding.ivUnFav.visibility == View.VISIBLE) {
                binding.ivFav.visibility = View.VISIBLE
                binding.ivUnFav.visibility = View.INVISIBLE
                val fav = FavoriteDb()
                fav.id = userData.id
                fav.username = userData.login
                fav.avatarUrl = userData.avatarUrl
                fav.favorite = true
                favUpdateViewModel.insert(fav)
                showToast(getString(R.string.added))
            }

        }

        binding.ivFav.setOnClickListener {
            val fav = FavoriteDb()
            fav.id = userData.id
            fav.username = userData.login
            fav.avatarUrl = userData.avatarUrl
            fav.favorite = false
            favUpdateViewModel.delete(fav)
            showToast(getString(R.string.remove))
            binding.ivUnFav.visibility = View.VISIBLE
            binding.ivFav.visibility = View.INVISIBLE

        }


    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fbReport.startAnimation(fromBottom)
            binding.fbShare.startAnimation(fromBottom)
            binding.fbAdd.startAnimation(rotateOpen)
        } else {
            binding.fbReport.startAnimation(toBottom)
            binding.fbShare.startAnimation(toBottom)
            binding.fbAdd.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fbReport.visibility = View.VISIBLE
            binding.fbShare.visibility = View.VISIBLE
        } else {
            binding.fbReport.visibility = View.INVISIBLE
            binding.fbShare.visibility = View.INVISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initData(user: String?) {
        showLoading(true)
        userDetailViewModel.setUserDetail(user!!, this)
        userDetailViewModel.getUserDetail().observe(this) { userResp ->
            if (userResp != null) {
                showLoading(false)
                userData = userResp
                if (userResp.name == null) {
                    binding.tvName.visibility = View.GONE
                }
                if (userResp.location == null) {
                    binding.tvLocation.visibility = View.GONE
                }
                if (userResp.company == null) {
                    binding.tvCompany.visibility = View.GONE
                }
                binding.tvName.text = userResp.name
                binding.tvRepo.text = userResp.publicRepos.toString()
                binding.tvLocation.text = userResp.location
                binding.tvCompany.text = userResp.company
                binding.tvFollower.text = userResp.followers.toString()
                binding.tvFollowing.text = userResp.following.toString()

                binding.ivUnFav.isEnabled = true
                binding.ivFav.isEnabled = true


                Glide.with(this@DetailUserActivity)
                    .load(userResp.avatarUrl)
                    .into(binding.ivUser)
            } else {
                showLoading(false)
            }
        }
    }

    private fun shareDetail(user: UserDetailResponse) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Halo..., mari berteman bersamaku di Github! ${user.login} / ${user.url}"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavUpdateViewModel::class.java)
    }
}