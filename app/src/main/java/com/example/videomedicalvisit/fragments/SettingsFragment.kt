package com.example.videomedicalvisit.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.EditProfileActivity
import com.example.videomedicalvisit.ProfileActivity
import com.example.videomedicalvisit.databinding.FragmentSettingsBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
   lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =  requireActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE)

         token = sharedPref.getString("token", "")!!
         getProfile()
        binding.modifyProfile.setOnClickListener {
            startActivity(Intent(requireActivity(),EditProfileActivity::class.java))
        }

        binding.profileIV.setOnClickListener {
            startActivity(Intent(requireActivity(),ProfileActivity::class.java))
        }

    }
    private fun getProfile(){
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,Constant.PROFILE, null,
            Response.Listener { response ->
                val success = response.getBoolean("success")
                if(success == true){
                    val user_details = response.getJSONObject("user_details")
                    binding.nameTV.text = user_details.getString("firstName") + user_details.getString("lastName")
                    binding.emailTV.text = user_details.getString("email")
                    val sex = user_details.getString("sex")

                    val profile_picture = user_details.getString("picture_url")
                    val default_picture = user_details.getString("default_picture_url")
                    if (profile_picture == ""){
                        Glide.with(requireActivity())
                            .load(default_picture)
                            .into(binding.profileImage)
                    }
                    else{
                        Glide.with(requireActivity())
                            .load(profile_picture)
                            .into(binding.profileImage)
                    }
                    if (!sex.isNullOrEmpty()){
                        binding.sexTV.text = sex
                    } else {
                        binding.sexTV.text = "N/A"
                    }
                }else{
                    val message = response.getString("message");
                    Toast.makeText(requireActivity(), "${message.toString()}", Toast.LENGTH_SHORT).show()
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
        VolleySingleton.getInstance(requireActivity()).add(jsonRequest)
    }
}