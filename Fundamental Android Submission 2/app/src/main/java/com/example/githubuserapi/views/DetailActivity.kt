package com.example.githubuserapi.views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.retrofit.Client
import com.example.githubuserapi.model.User
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var sectionPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val username: String? = intent.getStringExtra(EXTRA_NAME)
        setActionBar()
        getDetailApi(username)
        initSectionPager()
        sectionPagerAdapter.user = username
    }

    private fun initSectionPager(){
        sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getDetailApi(username : String?){
        val client = username?.let {
            Client.getApiServices().getDetailUser(it)
        }
        client?.enqueue(object : Callback<User>{

            override fun onResponse(call: Call<User>, response: Response<User>) {
                try {
                    val data = response.body()

                    Glide.with(this@DetailActivity)
                        .load(data?.avatar_url)
                        .apply(RequestOptions())
                        .into(img_detail_photo)

                    tv_detail_fullname.text = if (data?.name != null) data.name else "-"
                    tv_detail_username.text = if (data?.login != null) data.login else "-"
                    tv_detail_company.text = if (data?.company != null) data.company else "-"
                    tv_detail_location.text = if (data?.location != null) data.location else "-"
                    tv_detail_repo.text = if (data?.public_repos != null) data.public_repos else "-"
                    tv_detail_followers.text = if (data?.followers != null) data.followers else "-"
                    tv_detail_following.text = if (data?.following != null) data.following else "-"
                    progressBarDetail.visibility = View.INVISIBLE
                } catch (i : Exception){
                    Toast.makeText(this@DetailActivity, i.message, Toast.LENGTH_SHORT).show()
                    i.printStackTrace()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Gagal", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

        })
    }
    private fun setActionBar(){
        supportActionBar?.title = resources.getString(R.string.title_bar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
    }
}