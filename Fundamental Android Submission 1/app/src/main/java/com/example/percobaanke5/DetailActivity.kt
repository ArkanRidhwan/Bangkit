package com.example.percobaanke5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data = intent.getParcelableExtra<Data>(MainActivity.INTENT_PARCELABLE)

        val gambar = findViewById<ImageView>(R.id.img_detail)
        val follower= findViewById<TextView>(R.id.tv_detail_follower)
        val following = findViewById<TextView>(R.id.tv_detail_following)
        val company= findViewById<TextView>(R.id.tv_detail_company)
        val location = findViewById<TextView>(R.id.tv_detail_location)
        val repository= findViewById<TextView>(R.id.tv_detail_repository)

        gambar.setImageResource(data!!.gambar)
        follower.text = "Follower:  " + data.follower
        following.text = "Following:  "+data.following
        company.text = "Company:  "+data.company
        location.text = "Location:  "+data.location
        repository.text = "Repository:  "+data.repository
    }
}