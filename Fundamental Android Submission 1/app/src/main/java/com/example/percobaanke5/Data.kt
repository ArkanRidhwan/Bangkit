package com.example.percobaanke5

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    var gambar: Int,
    var nama: String,
    var username: String,
    var follower:String,
    var following: String,
    var company: String,
    var location: String,
    var repository: String
) : Parcelable

