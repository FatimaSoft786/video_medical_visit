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
                    val good_health = user_details.getString("good_health")
                    val serious_illness = user_details.getString("serious_illness")
                    val past_surgery = user_details.getString("past_surgery")
                    val current_medication = user_details.getString("current_medication")
                    val heart_disease = user_details.getString("heart_disease")
                    val blood_pressure = user_details.getString("blood_pressure")
                    val allergies = user_details.getString("allergies")
                    val diabetes = user_details.getString("diabetes")
                    val thyroid = user_details.getString("thyroid")
                    val stomach_disease= user_details.getString("stomach_disease")
                    val kidney_disease = user_details.getString("kidney_disease")
                    val digestive_disease = user_details.getString("digestive_disease")
                    val lung_disease = user_details.getString("lung_disease")
                    val venereal = user_details.getString("venereal")
                    val nervous = user_details.getString("nervous")
                    val hormone = user_details.getString("hormone")
                    val any_illness = user_details.getString("any_illness")
                    val smoke = user_details.getString("smoke")
                    val alcohol = user_details.getString("alcohol")
                    val usual = user_details.getString("usual_medicine")
                    binding.illED.setText(user_details.getString("serious_illness_description"))
                    binding.pastSurgeryED.setText(user_details.getString("past_surgery_description"))
                    binding.currentED.setText(user_details.getString("current_medication_description"))
                    binding.allergiesED.setText(user_details.getString("allergies_description"))
                    binding.digestiveED.setText(user_details.getString("digestive_description"))
                    binding.lungED.setText(user_details.getString("lungs_description"))
                    binding.usualED.setText(user_details.getString("usual_medicine_description"))


                    if(good_health.equals("Yes")){
                        binding.healthYes.isChecked = true
                    }else{
                        binding.healthNo.isChecked = true
                    }

                    if(serious_illness.equals("Yes")){
                        binding.illnessYes.isChecked = true
                    }else{
                        binding.illnessNo.isChecked = true
                    }

                    if(alcohol.equals("Yes")){
                        binding.alcoholYes.isChecked = true
                    }else{
                        binding.alcoholNo.isChecked = true
                    }

                    if(nervous.equals("Yes")){
                        binding.nervousYes.isChecked = true
                    }else{
                        binding.nervousNo.isChecked = true
                    }

                    if(smoke.equals("Yes")){
                        binding.smokeYes.isChecked = true
                    }else{
                        binding.smokeNo.isChecked = true
                    }

                    if(usual.equals("Yes")){
                        binding.usualYes.isChecked = true
                    }else{
                        binding.usualNo.isChecked = true
                    }

                    if(venereal.equals("Yes")){
                        binding.venerealYes.isChecked = true
                    }else{
                        binding.venerealNo.isChecked = true
                    }

                    if(lung_disease.equals("Yes")){
                        binding.lungYes.isChecked = true
                    }else{
                        binding.lungNo.isChecked = true
                    }
                    if(digestive_disease.equals("Yes")){
                        binding.digestiveYes.isChecked = true
                    }else{
                        binding.digestiveNo.isChecked = true
                    }
                    if(kidney_disease.equals("Yes")){
                        binding.kidneyDiseaseYes.isChecked = true
                    }else{
                        binding.kidneyDiseaseNo.isChecked = true
                    }
                    if(stomach_disease.equals("Yes")){
                        binding.stomachYes.isChecked = true
                    }else{
                        binding.stomachNo.isChecked = true
                    }
                    if(thyroid.equals("Yes")){
                        binding.thyroidYes.isChecked = true
                    }else{
                        binding.thyroidNo.isChecked = true
                    }
                    if(diabetes.equals("Yes")){
                        binding.diabetesYes.isChecked = true
                    }else{
                        binding.diabetesNo.isChecked = true
                    }
                    if(allergies.equals("Yes")){
                        binding.allergiesYes.isChecked = true
                    }else{
                        binding.allergiesNo.isChecked = true
                    }
                    if(blood_pressure.equals("Yes")){
                        binding.bloodPressureYes.isChecked = true
                    }else{
                        binding.bloodPressureNo.isChecked = true
                    }
                    if(heart_disease.equals("Yes")){
                        binding.heartDiseaseYes.isChecked = true
                    }else{
                        binding.heartDiseaseNo.isChecked = true
                    }
                    if(past_surgery.equals("Yes")){
                        binding.pastSurgeryYes.isChecked = true
                    }else{
                        binding.pastSurgeryNo.isChecked = true
                    }
                    if(current_medication.equals("Yes")){
                        binding.currentMedicationYes.isChecked = true
                    }else{
                        binding.currentMedicationNO.isChecked = true
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