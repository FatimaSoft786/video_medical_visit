package com.example.videomedicalvisit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.databinding.ActivityEditProfileBinding
import com.example.videomedicalvisit.utils.ApiService
import com.example.videomedicalvisit.utils.DoctorRes
import com.example.videomedicalvisit.utils.RetrofitClient
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var selectedImagePath: Uri? = null
    var gender = arrayListOf<String>("Donna","Uomo")
    lateinit var token: String
    lateinit var id: String
    lateinit var selectedSexOption: String
    lateinit var sharedPref: SharedPreferences
    private lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocale("it")
         sharedPref =  getSharedPreferences("MyPreferences", MODE_PRIVATE)
         token = sharedPref.getString("token", "")!!
         id = sharedPref.getString("id", "")!!
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
        binding.changeLT.setOnClickListener {
     openGallery()
        }
        binding.backIV.setOnClickListener {
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }

    }
    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 999)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            Glide.with(applicationContext).load(selectedImageUri).into(binding.profileImage)
             file = File(getRealPathFromUri(this, selectedImageUri))
            Log.d("FILE", "onActivityResult: ${file}")
            uploadProfileImage()
        }
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            cursor?.close()
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


    fun uploadProfileImage(){
        binding.PB1.visibility = View.VISIBLE
        binding.tv.visibility = View.GONE
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)
        val id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),id)

        val apiService = retrofit.create(ApiService::class.java)
        val token ="Bearer $token"
        Log.d("TAG", "uploadProfileImage: ${id}")
        Log.d("TAG", "uploadProfileImage: ${token}")
        Log.d("TAG", "uploadProfileImage: ${file}")
        Log.d("TAG", "uploadProfileImage: ${id}")
        val call = apiService.uploadPicture(body,id,token)
        Log.d("TAG", "uploadProfileImage: ${call}")
        call.enqueue(object : Callback<DoctorRes> {
            override fun onResponse(call: Call<DoctorRes>, response: retrofit2.Response<DoctorRes>) {
                if (response.isSuccessful) {
                    binding.PB1.visibility = View.GONE
                    binding.tv.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, "Profile picture updated, it takes few seconds to load in the app.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DoctorRes>, t: Throwable) {
                Log.d("TAG", "onResponse: ${t.message}")
                binding.PB1.visibility = View.GONE
                binding.tv.visibility = View.VISIBLE
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })


    }

}