package com.example.videomedicalvisit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.databinding.ActivityDoctorBinding
import com.example.videomedicalvisit.utils.ApiService
import com.example.videomedicalvisit.utils.DoctorRes
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class DoctorActivity : AppCompatActivity() {

    var gender = arrayListOf<String>("Donna","Uomo")
   var specialist = arrayListOf("Dermatologia","Cardiologia","Psicologia","Medicina dello Sport",
       "Fisiatria","Medicina del Lavoro","Medico di famiglia")

    lateinit var selectedSexOption: String
    lateinit var selectedSpecialistOption: String

    private  lateinit var binding: ActivityDoctorBinding
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocale("it")
        var aa = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            gender
        )
        var aa1 = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            specialist
        )

        binding.specialistSP.adapter = aa1
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

        binding.specialistSP.onItemSelectedListener = object :
            AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSpecialistOption = specialist[p2].toString()
            }
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }
     binding.backIV.setOnClickListener {
         finish()
     }
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
        binding.start.setOnClickListener{
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
                binding.startTV.text = formattedDate
            },year,month,day)
            datePicker.show()
        }
        binding.end.setOnClickListener{
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
                binding.endTV.text = formattedDate
            },year,month,day)
            datePicker.show()
        }
  binding.upload.setOnClickListener {
      openGallery()
  }
   binding.saveBTN.setOnClickListener {
       val emailAddress = binding.emailED.text.toString().trim()
        if (binding.emailED.text.toString().isEmpty()) {
       binding.emailED.error = "Please enter email"
   } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
       binding.emailED.error = "Email is Invalid"
   }else{
            registerDoctor(binding.firstED.text.toString().trim(),binding.lastED.text.toString().trim(),binding.emailED.text.toString().trim(),selectedSexOption,binding.dateTV.text.toString(),binding.postalED.text.toString().trim(),binding.startTV.text.toString(),binding.endTV.text.toString(),binding.recognitionED.text.toString().trim(),selectedSpecialistOption,binding.clinic.text.toString().trim(),"test","test","Doctor","test","test")
        }

   }

    }
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun openGallery() {
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

    fun registerDoctor(firstname: String, lastname: String,email: String, sex: String,dob: String,postalCode: String,start: String,end: String,special: String,specialists: String, clinic: String, about: String,education: String,role: String, uni: String, exp: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://video-medico-backend-production.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("cv", file.name, requestFile)

        val firstName = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),firstname)
        val lastName = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),lastname)
        val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),email)
        val sex = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),sex)
        val location = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),firstname)
        val dob = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dob)
        val postalCode = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),postalCode)
        val start = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),start)
        val end = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),end)
        val special = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),special)
        val specialist = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),specialists)
        val clinic = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),clinic)
        val about = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),about)
        val education = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),education)
        val role = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),role)
        val uni = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),uni)
        val exp = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),exp)

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.registerDoctor(body,firstName,lastName,email,sex,location,dob,postalCode,start,end,special,specialist,clinic,about,education,role,uni,exp)
        call.enqueue(object : Callback<DoctorRes>{
            override fun onResponse(call: Call<DoctorRes>, response: Response<DoctorRes>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Doctor registered", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DoctorRes>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

}