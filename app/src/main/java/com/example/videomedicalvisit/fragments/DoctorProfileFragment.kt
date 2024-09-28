package com.example.videomedicalvisit.fragments

import android.content.SharedPreferences
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
import com.example.videomedicalvisit.R
import com.example.videomedicalvisit.databinding.FragmentDoctorProfileBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton


class DoctorProfileFragment : Fragment() {
     private lateinit var binding: FragmentDoctorProfileBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoctorProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref =  requireActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE)

        token = sharedPref.getString("token", "")!!
        getProfile()
    }

    private fun getProfile(){
        val editor = sharedPref.edit()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.PROFILE, null,
            Response.Listener { response ->
                val success = response.getBoolean("success")
                if(success == true) {
                    val user_details = response.getJSONObject("user_details")
                    binding.first.setText(user_details.getString("firstName"))
                    binding.last.setText(user_details.getString("lastName"))
                    binding.email.setText(user_details.getString("email"))
                   // binding.mobileED.setText(user_details.getString("phoneNumber"))
                    binding.location.setText(user_details.getString("location"))
                    binding.code.setText(user_details.getString("postal_code"))
                    binding.dateTV.setText(user_details.getString("dob"))
                    binding.startTV.setText(user_details.getString("studies_start_year"))
                    binding.endTV.setText(user_details.getString("studies_end_year"))
                    binding.specialRecognition.setText(user_details.getString("special_recognition"))
                    binding.sex.setText(user_details.getString("sex"))
                    binding.clinic.setText(user_details.getString("clinic_hospital_address"))
                    binding.specialist.setText(user_details.getString("specialist"))
                    editor.putString("id", user_details.getString("_id"))
                    editor.apply()
                    binding.sex.setText(user_details.getString("sex"))
                    val profile_picture = user_details.getString("picture_url")
                    val default_picture = user_details.getString("default_picture_url")
                    if (profile_picture == "") {
                        Glide.with(requireActivity())
                            .load(default_picture)
                            .into(binding.profileImage)
                    } else {
                        Glide.with(requireActivity())
                            .load(profile_picture)
                            .into(binding.profileImage)
                    }


                }

                else{
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