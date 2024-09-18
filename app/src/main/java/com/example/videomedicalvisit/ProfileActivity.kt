package com.example.videomedicalvisit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.databinding.ActivityProfileBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.anamnesiTV.setOnClickListener {
            startActivity(Intent(applicationContext,EditConsentFormActivity::class.java))
        }
        sharedPref =  getSharedPreferences("MyPreferences", MODE_PRIVATE)

        token = sharedPref.getString("token", "")!!
        getProfile()
    }
    private fun getProfile(){
        val editor = sharedPref.edit()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.PROFILE, null,
            Response.Listener { response ->
                val success = response.getBoolean("success")
                if(success == true){
                    val user_details = response.getJSONObject("user_details")
                    binding.firstED.setText(user_details.getString("firstName"))
                    binding.lastED.setText(user_details.getString("lastName"))
                    binding.emailED.setText(user_details.getString("email"))
                    binding.mobileED.setText(user_details.getString("phoneNumber"))
                    binding.cityED.setText(user_details.getString("location"))
                    binding.codeED.setText(user_details.getString("postal_code"))
                    binding.dateTV.setText(user_details.getString("dob"))
                    editor.putString("id",user_details.getString("_id"))
                    editor.apply()
                    binding.sex.setText(user_details.getString("sex"))
                    val profile_picture = user_details.getString("picture_url")
                    val default_picture = user_details.getString("default_picture_url")
                    if (profile_picture == ""){
                        Glide.with(applicationContext)
                            .load(default_picture)
                            .into(binding.profileImage)
                    }
                    else{
                        Glide.with(applicationContext)
                            .load(profile_picture)
                            .into(binding.profileImage)
                    }

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