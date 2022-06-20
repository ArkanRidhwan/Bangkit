package com.example.percobaanke5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    companion object{
        const val INTENT_PARCELABLE = "OBJECT_INTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageList = listOf(
            Data(
                R.drawable.user1,
                "JakeWharton",
                "Jake Wharton",
                "56995",
                "12",
                "Google, Inc.",
                "Pittsburgh, PA, USA",
                "102",
            ),
            Data(
                R.drawable.user2,
                "Amit Shekhar",
                "amitshekhariitbhu",
                "5153",
                "2",
                "MindOrksOpenSource",
                "New Delhi, India",
                "37",
            ),
            Data(
                R.drawable.user3,
                "Romain Guy",
                "romainguy",
                "7972",
                "0",
                "Google",
                "California",
                "9",
            ),
            Data(
                R.drawable.user4,
                "Chris Banes",
                "chrisbanes",
                "14725",
                "1",
                "Google working on android",
                "Sydney, Australia",
                "30",
            ),
            Data(
                R.drawable.user5,
                "David",
                "tipsy",
                "788",
                "0",
                "Working Group Two",
                "Trondheim, Norway",
                "56",
            ),
            Data(
                R.drawable.user6,
                "Ravi Tamada",
                "ravi8x",
                "18628",
                "3",
                "AndroidHive | Droid5",
                "India",
                "28",
            ),
            Data(
                R.drawable.user7,
                "Deny Prasetyo",
                "jasoet",
                "277",
                "39",
                "AndroidHive | Droid5",
                "Kotagede, Yogyakarta, Indonesia",
                "44",
            ),
            Data(
                R.drawable.user8,
                "Budi Oktaviyan",
                "budioktaviyan",
                "178",
                "23",
                "AndroidHive | Droid5",
                "Jakarta, Indonesia",
                "110",
            ),
            Data(
                R.drawable.user9,
                "Hendi Santika",
                "hendisantika",
                "428",
                "61",
                "JVMDeveloperID @KotlinID @IDDevOps",
                "Bojongsoang - Bandung Jawa Barat",
                "1064",
            ),
            Data(
                R.drawable.user10,
                "Sidiq Permana",
                "sidiqpermana",
                "465",
                "10",
                "Nusantara Beta Studio",
                "Jakarta Indonesia",
                "65",
            )
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = Adapter(this, imageList){
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(INTENT_PARCELABLE, it)
            startActivity(intent)
        }
    }
}