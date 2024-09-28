package com.example.videomedicalvisit.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.videomedicalvisit.adapter.FavoriteAdapter
import com.example.videomedicalvisit.adapter.UserAdapter
import com.example.videomedicalvisit.databinding.FragmentFavoriteBinding
import com.example.videomedicalvisit.model.User
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import com.project.farmingapp.utilities.CellClickListener
import org.json.JSONArray
import org.json.JSONObject


class FavoriteFragment : Fragment(), CellClickListener {

    private lateinit var binding: FragmentFavoriteBinding
    lateinit var token: String
    lateinit var id: String
    lateinit var sharedPref: SharedPreferences
    private var usersList = ArrayList<User>()
    private lateinit var adapter: FavoriteAdapter
    private lateinit var patientId: String
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref =  mContext!!.getSharedPreferences("MyPreferences", MODE_PRIVATE)
        token = sharedPref.getString("token", "")!!
        patientId = sharedPref.getString("id","")!!
       getFavorites()
    }
  fun getFavoriteDoctors(doctors: List<User>, favorites: List<String>, myId: String): List<User>{
      return  if(favorites.contains(myId)){
          doctors.filter { favorites.contains(it._id) }
      }else {
          emptyList()
      }
  }

    override fun onCellClickListener(index: String) {

      //  Toast.makeText(requireActivity(), "${index}", Toast.LENGTH_SHORT).show()
            val jsonObj = JSONObject()
            jsonObj.put("doctorId",index)
            val jsonRequest = object : JsonObjectRequest(
                Request.Method.POST, Constant.DELETEFAVORITE,jsonObj,
                Response.Listener { response ->
                   try {
                       Log.d("FAvorite", "onCellClickListener: ${response}")
                       val success = response.getBoolean("success")
                       if(success == true){
                           getFavorites()
                           // val message = response.getString("message");
                           //Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
                       }else{

                           val message = response.getString("message");
                           Toast.makeText(mContext!!, "${message.toString()}", Toast.LENGTH_SHORT).show()
                       }
                   }catch (e: Exception){
                       e.printStackTrace()
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
        Log.d("FAvorite", "onCellClickListener: ${jsonRequest}")
        Log.d("FAvorite", "onCellClickListener: ${jsonObj}")
            VolleySingleton.getInstance(mContext!!).add(jsonRequest)
        }

   private fun getFavorites(){
       usersList.clear()
       val jsonObj = JSONObject()
       jsonObj.put("role","Doctor")

       val jsonRequest = object : JsonObjectRequest(
           Request.Method.POST, Constant.DOCTORSLIST,jsonObj,
           Response.Listener { response ->
            try {
                Log.d("TAG", "onViewCreated: ${response}")
                val array: JSONArray = response.getJSONArray("doctors_list")
                for (i in 0 until array.length()) {
                    val jsonOBJ = array.getJSONObject(i)
                    val user = User(jsonOBJ.getString("firstName"),
                        jsonOBJ.getString("lastName"),
                        jsonOBJ.getString("_id"),
                        jsonOBJ.getString("default_picture_url"),
                        jsonOBJ.getString("picture_url"),
                        jsonOBJ.getString("location"),
                        jsonOBJ.getString("specialist"),
                        jsonOBJ.getString("favorites"))
                    if(user.favorites.contains(patientId)){
                        usersList.add(user)
                    }

                    adapter = FavoriteAdapter(mContext!!,this)
                    adapter.setListData(usersList)
                    binding.RV.adapter = adapter

                }
            }catch (e:Exception){
                e.printStackTrace()
            }

           },
           Response.ErrorListener { error ->

//                binding.PB.visibility = View.GONE
//                binding.TV.visibility = View.VISIBLE
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
       VolleySingleton.getInstance(mContext!!).add(jsonRequest)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    }


