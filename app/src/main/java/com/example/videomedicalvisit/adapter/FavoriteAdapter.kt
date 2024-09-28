package com.example.videomedicalvisit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.R
import com.example.videomedicalvisit.databinding.UserItemBinding
import com.example.videomedicalvisit.model.User
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import com.project.farmingapp.utilities.CellClickListener
import org.json.JSONObject

class FavoriteAdapter(var context: Context, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var userList = mutableListOf<User>()
    private  lateinit var  sharedPrefs: SharedPreferences
    private  lateinit var token : String
    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: MutableList<User>) {
        userList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameTV = binding.name
        var profileIV = binding.profile
        var locationTV = binding.location
        var specialist = binding.specialistTV
        var like = binding.like



    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        sharedPrefs =  holder.itemView.context.getSharedPreferences("MyPreferences", MODE_PRIVATE)
        token = sharedPrefs.getString("token", "")!!
        with(holder) {
            with(userList[position]) {
                holder.nameTV.text = this.firstName +" "+ this.lastName
                Glide.with(holder.itemView).load(this.default_picture_url).into(profileIV)
                holder.locationTV.text = this.location
                holder.specialist.text = this.specialist

                Glide.with(holder.itemView.context).load(R.drawable.baseline_favorite_24).into(holder.like)

//                if(this.favorites.contains(patientId)){
//
//                }else{
//                    Glide.with(holder.itemView.context).load(R.drawable.un_fill_favorite).into(holder.like)
//                }
       holder.itemView.setOnClickListener {
           cellClickListener.onCellClickListener(this._id.toString())
       }


            }
        }
    }
    override fun getItemCount(): Int {
        return  userList.size
    }


}