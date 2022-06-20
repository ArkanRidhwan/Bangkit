package com.example.percobaanke5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val context: Context,
    private val images: List<Data>,
    private val listener: (Data) -> Unit
) : RecyclerView.Adapter<Adapter.ImageViewHolder>() {
    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_item_photo)
        private val nama: TextView = view.findViewById(R.id.tv_item_name)
        private val username: TextView = view.findViewById(R.id.tv_item_username)
        fun bindView(data: Data, listener: (Data) -> Unit) {
            image.setImageResource(data.gambar)
            nama.text = data.nama
            username.text = data.username
            itemView.setOnClickListener { listener(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.list, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(images[position], listener)
    }
}