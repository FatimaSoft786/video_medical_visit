package com.example.videomedicalvisit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.example.videomedicalvisit.databinding.ActivityDoctorDashboardBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton

class DoctorDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorDashboardBinding
    lateinit var token: String
    lateinit var sharedPref: SharedPreferences
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref =  getSharedPreferences("MyPreferences", MODE_PRIVATE)
        token = sharedPref.getString("token", "")!!
        getProfile()
        binding.nameTV.setOnClickListener {
            startActivity(Intent(applicationContext,DoctorProfileActivity::class.java))
        }

    }
    private fun getProfile(){
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.PROFILE, null,
            Response.Listener { response ->
                val success = response.getBoolean("success")
                if(success == true){
                    val user_details = response.getJSONObject("user_details")
                    binding.nameTV.text = getString(R.string.hi) + user_details.getString("firstName") +" "+ user_details.getString("lastName")
                }else{
                    val message = response.getString("message");
                    Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
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
        VolleySingleton.getInstance(applicationContext).add(jsonRequest)
    }
}