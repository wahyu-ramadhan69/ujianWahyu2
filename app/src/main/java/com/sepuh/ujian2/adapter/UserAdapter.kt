package com.sepuh.ujian2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sepuh.ujian2.R
import com.sepuh.ujian2.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: List<User> = emptyList()
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onEditClickListener: OnEditClickListener? = null


    interface OnDeleteClickListener {
        fun onDeleteClick(user: User)
    }

    interface OnEditClickListener {
        fun onEditClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_data, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)

        holder.btnDelete.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(user)
        }

        holder.btnEdit.setOnClickListener {
            onEditClickListener?.onEditClick(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        onEditClickListener = listener
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nama)
        private val addressTextView: TextView = itemView.findViewById(R.id.alamat)
        private val outstandingTextView: TextView = itemView.findViewById(R.id.outstanding)
        val btnDelete: Button = itemView.findViewById(R.id.btnHapus)
        val btnEdit: ImageView = itemView.findViewById(R.id.editBtn)

        fun bind(user: User) {
            nameTextView.text = user.nama
            addressTextView.text = user.alamat
            outstandingTextView.text = "Rp ${user.jumlah_outstanding}"
        }
    }
}
