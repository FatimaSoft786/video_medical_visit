package com.example.videomedicalvisit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.databinding.SearchResultItemBinding
import com.example.videomedicalvisit.model.User

class SearchResultAdapter(var context: Context) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    private var specialistList = mutableListOf<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: MutableList<User>) {
        specialistList  = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameTV = binding.name
        var specialistTV = binding.specialist
        var profile = binding.profile


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchResultItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        with(holder) {
            with(specialistList[position]) {
                holder.specialistTV.text = this.specialist
                holder.nameTV.text = this.firstName +" "+ this.lastName
                 if(this.picture_url == ""){
                     Glide.with(holder.itemView).load(this.default_picture_url).into(profile)
                 }else{
                     Glide.with(holder.itemView).load(this.picture_url).into(profile)
                 }

            }
        }
    }
    override fun getItemCount(): Int {
        return  specialistList.size
    }


}