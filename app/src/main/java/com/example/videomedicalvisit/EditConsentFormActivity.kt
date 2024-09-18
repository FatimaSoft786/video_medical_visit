package com.example.videomedicalvisit

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.videomedicalvisit.databinding.ActivityEditConsentFormBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import org.json.JSONObject

class EditConsentFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditConsentFormBinding
    private var good_health: String = ""
    private var serious_illness: String = ""
    private var serious_illness_description: String = ""
    private var past_surgery: String = ""
    private var past_surgery_description: String = ""
    private var current_medication: String = ""
    private var current_medication_description: String = ""
    private var heart_disease: String = ""
    private var blood_pressure: String = ""
    private var allergies: String = ""
    private var allergies_description: String = ""
    private var diabetes: String = ""
    private var kidney_disease: String = ""
    private var thyroid: String = ""
    private var stomach_disease: String = ""
    private var digestive_disease: String = ""
    private var digestive_description: String = ""
    private var lung_disease: String = ""
    private var lungs_description: String = ""
    private var venereal: String = ""
    private var nervous: String = ""
    private var hormone: String = ""
    private var any_illness: String = ""
    private var any_illness_description: String = ""
    private var smoke: String = ""
    private var alcohol: String = ""
    private var usual_medicine: String = ""
    private var usual_medicine_description: String = ""
    lateinit var token: String
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditConsentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref =  getSharedPreferences("MyPreferences", MODE_PRIVATE)

        token = sharedPref.getString("token", "")!!
        medicalFormCheckboxes()
        binding.save.setOnClickListener {

           medicalHistory()
        }

    }
    private fun medicalHistory(){
        val id = sharedPref.getString("id", "")!!
        val jsonObj = JSONObject()
        jsonObj.put("patientId",id)

        jsonObj.put("good_health",good_health)
        jsonObj.put("serious_illness",serious_illness)
        jsonObj.put("serious_illness_description",binding.illED.text.toString().trim())
        jsonObj.put("past_surgery",past_surgery)
        jsonObj.put("past_surgery_description",binding.pastSurgeryED.text.toString().trim())
        jsonObj.put("current_medication",current_medication)
        jsonObj.put("current_medication_description", binding.currentED.text.toString())
        jsonObj.put("heart_disease",heart_disease)
        jsonObj.put("blood_pressure",blood_pressure)
        jsonObj.put("allergies",allergies)
        jsonObj.put("heart_disease",heart_disease)
        jsonObj.put("allergies_description",binding.allergiesED.text.toString().trim())
        jsonObj.put("diabetes",diabetes)
        jsonObj.put("kidney_disease",kidney_disease)
        jsonObj.put("thyroid",thyroid)
        jsonObj.put("stomach_disease",stomach_disease)
        jsonObj.put("digestive_disease",digestive_disease)
        jsonObj.put("digestive_description",binding.digestiveED.text.toString().trim())
        jsonObj.put("lung_disease",lung_disease)
        jsonObj.put("lungs_description", binding.lungED.text.toString().trim())
        jsonObj.put("venereal",venereal)
        jsonObj.put("nervous",nervous)
        jsonObj.put("hormone", "No")
        jsonObj.put("any_illness", "No")
        jsonObj.put("any_illness_description","No")
        jsonObj.put("smoke",smoke)
        jsonObj.put("alcohol",alcohol)
        jsonObj.put("usual_medicine",usual_medicine)
        jsonObj.put("usual_medicine_description",binding.usualED.text.toString().trim())

        binding.PB.visibility = View.VISIBLE
        binding.TV.visibility = View.GONE
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.PATIENTMEDICALHISTORY,jsonObj,
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

    private fun medicalFormCheckboxes() {
        //health
        binding.healthYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                good_health = "Yes"
                binding.healthNo.isChecked = false
            }
        }
        binding.healthNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                good_health = "No"
                binding.healthYes.isChecked = false
            }
        }
     //serious
     binding.illnessYes.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
       serious_illness = "Yes"
       binding.illnessNo.isChecked = false
      }
     }
     binding.illnessNo.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
       serious_illness = "No"
       binding.illnessYes.isChecked = false
      }
     }
     //past surgery
     binding.pastSurgeryYes.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
       past_surgery = "Yes"
       binding.pastSurgeryNo.isChecked = false
      }
     }
     binding.pastSurgeryNo.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
       past_surgery = "No"
       binding.pastSurgeryYes.isChecked = false
      }
     }
     //current medication
     binding.currentMedicationYes.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
          current_medication = "Yes"
       binding.currentMedicationNO.isChecked = false
      }
     }
     binding.currentMedicationNO.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
       current_medication = "No"
       binding.currentMedicationYes.isChecked = false
      }
     }
        //heart disease
        binding.heartDiseaseYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                heart_disease = "Yes"
                binding.heartDiseaseNo.isChecked = false
            }
        }
        binding.heartDiseaseNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                heart_disease = "No"
                binding.heartDiseaseYes.isChecked = false
            }
        }
        //blood pressure
        binding.bloodPressureYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                blood_pressure = "Yes"
                binding.bloodPressureNo.isChecked = false
            }
        }
        binding.bloodPressureNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                blood_pressure = "No"
                binding.bloodPressureYes.isChecked = false
            }
        }
        //allergies
        binding.allergiesYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                allergies = "Yes"
                binding.allergiesNo.isChecked = false
            }
        }
        binding.allergiesNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                allergies = "No"
                binding.allergiesYes.isChecked = false
            }
        }
        //diabetes
        binding.diabetesYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                diabetes = "Yes"
                binding.diabetesNo.isChecked = false
            }
        }
        binding.diabetesNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                diabetes = "No"
                binding.diabetesYes.isChecked = false
            }
        }
        // kidney disease
        binding.kidneyDiseaseYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                kidney_disease = "Yes"
                binding.kidneyDiseaseNo.isChecked = false
            }
        }
        binding.kidneyDiseaseNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                kidney_disease = "No"
                binding.kidneyDiseaseYes.isChecked = false
            }
        }
        //thyroid
        binding.thyroidYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thyroid = "Yes"
                binding.thyroidNo.isChecked = false
            }
        }
        binding.thyroidNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                thyroid = "No"
                binding.thyroidYes.isChecked = false
            }
        }
        //stomach disease
        binding.stomachYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                stomach_disease = "Yes"
                binding.stomachNo.isChecked = false
            }
        }
        binding.stomachNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                stomach_disease = "No"
                binding.stomachYes.isChecked = false
            }
        }
        // digestive disease
        binding.digestiveYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                digestive_disease = "Yes"
                binding.digestiveNo.isChecked = false
            }
        }
        binding.digestiveNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                digestive_disease = "No"
                binding.digestiveYes.isChecked = false
            }
        }
        // lungs disease
        binding.lungYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                lung_disease = "Yes"
                binding.lungNo.isChecked = false
            }
        }
        binding.lungNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                lung_disease = "No"
                binding.lungYes.isChecked = false
            }
        }
        // veneral
        binding.venerealYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                venereal = "Yes"
                binding.venerealNo.isChecked = false
            }
        }
        binding.venerealNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                venereal = "No"
                binding.venerealYes.isChecked = false
            }
        }
        // hormone
        binding.nervousYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                nervous = "Yes"
                binding.nervousNo.isChecked = false
            }
        }
        binding.nervousNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                nervous = "No"
                binding.nervousYes.isChecked = false
            }
        }
        // alcohol
        binding.alcoholYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alcohol = "Yes"
                binding.alcoholNo.isChecked = false
            }
        }
        binding.alcoholNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alcohol = "No"
                binding.alcoholYes.isChecked = false
            }
        }
        // smoke
        binding.smokeYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                smoke = "Yes"
                binding.smokeNo.isChecked = false
            }
        }
        binding.smokeNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                smoke = "No"
                binding.smokeYes.isChecked = false
            }
        }

        // unusual disease
        binding.usualYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                usual_medicine = "Yes"
                binding.usualNo.isChecked = false
            }
        }
        binding.usualNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                usual_medicine = "No"
                binding.usualYes.isChecked = false
            }
        }





    }
}