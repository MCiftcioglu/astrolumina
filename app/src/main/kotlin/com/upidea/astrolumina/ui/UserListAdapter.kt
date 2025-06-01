package com.upidea.astrolumina.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.UserEntity

class UserListAdapter(
    private val userList: List<UserEntity>,
    private val onItemClick: ((UserEntity) -> Unit)? = null
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.textName)
        val textSign: TextView = view.findViewById(R.id.textSign)
        val textStatus: TextView = view.findViewById(R.id.textStatus)
        val imageStatus: ImageView = view.findViewById(R.id.imageStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textName.text = user.name
        holder.textSign.text = "Burç: ${user.sunSign}"
        holder.textStatus.text = if (user.isOnline) "Çevrimiçi" else "Çevrimdışı"

        val statusIcon = if (user.isOnline) R.drawable.online_dot else R.drawable.offline_dot
        holder.imageStatus.setImageResource(statusIcon)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

    override fun getItemCount() = userList.size
}
