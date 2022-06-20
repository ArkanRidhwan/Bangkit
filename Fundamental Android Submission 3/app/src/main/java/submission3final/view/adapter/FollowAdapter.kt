package submission3final.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission3final.githubuserapp.R
import submission3final.model.response.FollowResponse

class FollowAdapter(private val mContext: Context, private val listUser: List<FollowResponse>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_follow, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(mContext)
            .load(listUser[position].avatarUrl)
            .into(holder.imgUser)
        holder.tvUsername.text = listUser[position].login
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgUser: ImageView = itemView.findViewById(R.id.iv_profileFW)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_usernameFW)
    }

}