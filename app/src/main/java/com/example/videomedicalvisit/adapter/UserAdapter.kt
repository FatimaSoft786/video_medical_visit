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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.R
import com.example.videomedicalvisit.databinding.UserItemBinding
import com.example.videomedicalvisit.model.User
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import okhttp3.internal.format
import org.json.JSONArray
import org.json.JSONObject

class UserAdapter(var context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var userList = mutableListOf<User>()
    private  lateinit var  sharedPrefs: SharedPreferences
    private  lateinit var token : String
    private lateinit var patientId: String
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
        patientId = sharedPrefs.getString("id","")!!
        with(holder) {
            with(userList[position]) {
                holder.nameTV.text = this.firstName +" "+ this.lastName
                Glide.with(holder.itemView).load(this.default_picture_url).into(profileIV)
                holder.locationTV.text = this.location
                holder.specialist.text = this.specialist

                if(this.favorites.contains(patientId)){
                    Glide.with(holder.itemView.context).load(R.drawable.baseline_favorite_24).into(holder.like)
                }else{
                    Glide.with(holder.itemView.context).load(R.drawable.un_fill_favorite).into(holder.like)
                }

                 holder.like.setOnClickListener {
                     sharedPrefs =  holder.itemView.context.getSharedPreferences("MyPreferences", MODE_PRIVATE)
                     token = sharedPrefs.getString("token", "")!!
                     patientId = sharedPrefs.getString("id","")!!
                     val jsonObj = JSONObject()
                     jsonObj.put("doctorId",this._id)
                     jsonObj.put("patientId",patientId)
                     val jsonRequest = object : JsonObjectRequest(
                         Method.POST, Constant.LIKE,jsonObj,
                         Response.Listener { response ->
                             Log.d("TAG", "onBindViewHolder: ${response}")
                             val success = response.getBoolean("success")
                if(success == true){
                    Glide.with(holder.itemView.context).load(R.drawable.baseline_favorite_24).into(holder.like)
                    val message = response.getString("message");
                    Toast.makeText(holder.itemView.context,message, Toast.LENGTH_SHORT).show()
                }else{

                    val message = response.getString("message");
                    Toast.makeText(holder.itemView.context, "${message.toString()}", Toast.LENGTH_SHORT).show()
                }
                         },
                         Response.ErrorListener { error ->
                             println("Error: ${error.message}")
                         }
                     ) {
                         override fun getHeaders(): Map<String, String> {
                             val headers = HashMap<String, String>()
                             headers["Authorization"] = "Bearer $token"
                             headers["Content-Type"] = "application/json"
                             return headers
                         }
                     }
                     VolleySingleton.getInstance(holder.itemView.context).add(jsonRequest)
                 }

            }
        }
    }
    override fun getItemCount(): Int {
        return  userList.size
    }


}