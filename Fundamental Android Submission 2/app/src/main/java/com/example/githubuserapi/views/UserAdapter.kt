package com.example.githubuserapi.views

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class UserAdapter(private var list : ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listUser = list[position]

        Glide.with(holder.itemView.context)
            .load(listUser.avatar_url)
            .apply(RequestOptions().override(70,70))
            .into(holder.imgPhoto)
        holder.tvNama.text = listUser.login
        holder.tvUsername.text = listUser.login

        val mContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val user = User(
                listUser.login,
                listUser.name,
                listUser.avatar_url,
                listUser.location,
                listUser.company,
                listUser.followers,
                listUser.following,
                listUser.public_repos,
                listUser.html_url,
                listUser.id
            )

            val moveToDetail = Intent(mContext, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_NAME, user.login)
            mContext.startActivity(moveToDetail)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgPhoto : ImageView = itemView.img_photo
        var tvUsername : TextView = itemView.tv_username
        var tvNama : TextView = itemView.tv_fullName
    }

    fun setData(user : ArrayList<User>){
        list.addAll(user)
        notifyDataSetChanged()
    }
}