package com.example.videomedicalvisit

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.databinding.ActivityEditProfileBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton

import com.google.android.gms.cast.framework.media.ImagePicker
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var selectedImagePath: Uri? = null
    var gender = arrayListOf<String>("Donna","Uomo")
    lateinit var token: String
    lateinit var selectedSexOption: String
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocale("it")
         sharedPref =  getSharedPreferences("MyPreferences", MODE_PRIVATE)
         token = sharedPref.getString("token", "")!!
        binding.calendar.setOnClickListener{
           val calendar = Calendar.getInstance()
           val year = calendar.get(Calendar.YEAR)
           val month = calendar.get(Calendar.MONTH)
           val day = calendar.get(Calendar.DAY_OF_MONTH)
           val datePicker = DatePickerDialog(this,{ _, selectedYear, selectedMonth, selectedDay ->
               val selectedCalendar = Calendar.getInstance();
               selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
               val localeItaly = Locale("it","IT")
               val dateFormat = SimpleDateFormat("dd/MM/yyyy",localeItaly)
               val formattedDate = dateFormat.format(selectedCalendar.time)
               binding.dateTV.text = formattedDate
           },year,month,day)
           datePicker.show()
       }

        var aa = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            gender
        )

        binding.sex.adapter = aa

        binding.sex.onItemSelectedListener = object :
            AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
       selectedSexOption = gender[p2].toString()
            }
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }

        getProfile()
     binding.change.setOnClickListener {
         editProfile()
     }
        binding.change.setOnClickListener {

        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun editProfile(){
     val id = sharedPref.getString("id", "")!!
        val jsonObj = JSONObject()
        jsonObj.put("patientId",id)
        jsonObj.put("firstName",binding.firstED.text.toString().trim())
        jsonObj.put("lastName",binding.lastED.text.toString().trim())
        jsonObj.put("email",binding.emailED.text.toString().trim())
        jsonObj.put("phoneNumber",binding.mobileED.text.toString().trim())
        jsonObj.put("location",binding.cityED.text.toString().trim())
        jsonObj.put("postal_code",binding.codeED.text.toString().trim())
        jsonObj.put("dob", binding.dateTV.text.toString())
        jsonObj.put("sex",selectedSexOption)
        binding.PB.visibility = View.VISIBLE
        binding.TV.visibility = View.GONE
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.EDITPROFILE,jsonObj,
            Response.Listener { response ->
                binding.PB.visibility = View.GONE
                binding.TV.visibility = View.VISIBLE
                val success = response.getBoolean("success")
                if(success == true){
                    Toast.makeText(applicationContext, "Dati salvati con successo!", Toast.LENGTH_SHORT).show()
                }else{
                    binding.PB.visibility = View.GONE
                    binding.TV.visibility = View.VISIBLE
                    val message = response.getString("message");
                    Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                binding.PB.visibility = View.GONE
                binding.TV.visibility = View.VISIBLE
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
    private fun getProfile(){
        val editor = sharedPref.edit()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,Constant.PROFILE, null,
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
                    binding.sex.setSelection(gender.indexOf(user_details.getString("sex")))
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