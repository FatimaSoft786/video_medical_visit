package com.example.videomedicalvisit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpanned
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.videomedicalvisit.databinding.ActivityLoginBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.showIV.setOnClickListener {
            if (isPasswordVisible) {
                // Hide password
                binding.passwordED.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showIV.setImageResource(R.drawable.pass_visible)
            } else {
                // Show password
                binding.passwordED.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showIV.setImageResource(R.drawable.baseline_remove_red_eye_24)
            }
            // Move the cursor to the end of the text
            binding.passwordED.setSelection(binding.passwordED.text.length)
            isPasswordVisible = !isPasswordVisible
        }
        binding.login.setOnClickListener {
       login()
        }
        binding.doctor.setOnClickListener {
            startActivity(Intent(applicationContext,DoctorActivity::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(applicationContext,SignUpActivity::class.java))
            finish()
        }
        binding.forgotTV.setOnClickListener {
          checkEmailBottomSheet()
        }
    }
    private fun login() {
        val emailAddress = binding.emailED.text.toString().trim()
        if (binding.emailED.text.toString().isEmpty()) {
            binding.emailED.error = "Please enter email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            binding.emailED.error = "Email is Invalid"
        } else if (binding.passwordED.text.toString().isEmpty()) {
            binding.passwordED.error = "Please enter password"
        } else if (binding.passwordED.text.toString().length < 6) {
            binding.passwordED.error = "Password length must be 6 characters"
        } else {
            binding.PB.visibility = View.VISIBLE
            binding.TV.visibility = View.GONE
            val jsonObject = JSONObject()
            jsonObject.put("email", binding.emailED.text.toString().trim())
            jsonObject.put("password",binding.passwordED.text.toString().trim())

            //shared preferences
            val sharedPref = getSharedPreferences("MyPreferences", MODE_PRIVATE)
            val editor = sharedPref.edit()


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                Constant.LOGIN_API,
                jsonObject,
                { response ->
                    Log.d("TAG", "login: ${response}")
                    binding.PB.visibility = View.GONE
                    binding.TV.visibility = View.VISIBLE
                    val success = response.getBoolean("success")
                    if(success == true){
                        val token = response.getString("accessToken")
                        val role = response.getString("role")
                        val id = response.getString("id")
                        editor.putString("token",token)
                        editor.putString("id",id)
                        editor.apply()
                        if(role == "Patient"){
                            startActivity(Intent(applicationContext,MainActivity::class.java))
                        }else {
                            startActivity(Intent(applicationContext,DoctorDashboardActivity::class.java))
                        }

                    }else{
                        val message = response.getString("message");
                        Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                    }

                },
                { error ->
                    println("response ${error.message}")
//                    val responseBody = String(error.networkResponse.data, charset("utf-8"))
//                    val data = JSONObject(responseBody)
//                    val message = data.optString("message")

                }
            )
            // Add the request to the RequestQueue
            VolleySingleton.getInstance(this).add(jsonObjectRequest)
        }
    }

    private fun checkEmailBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.check_email, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        val btn = view.findViewById<RelativeLayout>(R.id.emailConti)
        val ed = view.findViewById<EditText>(R.id.email)
        val PB = view.findViewById<ProgressBar>(R.id.PB)
        val TV = view.findViewById<TextView>(R.id.TV)
        btn.setOnClickListener {
            val emailAddress = ed.text.toString().trim()
            if (ed.text.toString().isEmpty()) {
                ed.error = "Please enter email"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                ed.error = "Email is Invalid"
            } else {
                PB.visibility = View.VISIBLE
                TV.visibility = View.GONE
                val jsonObject = JSONObject()
                jsonObject.put("email", ed.text.toString().trim())
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST,
                    Constant.CHECKEMAIL,
                    jsonObject,
                    { response ->

                        PB.visibility = View.GONE
                       TV.visibility = View.VISIBLE
                        val success = response.getBoolean("success")
                        if(success == true){
                            val message = response.getString("message")
                            Toast.makeText(applicationContext,"${message}", Toast.LENGTH_SHORT).show()
                            checkOtpBottomSheet()
                        }else{
                            val message = response.getString("message");
                            Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                        }

                    },
                    { error ->
                        println("response ${error.message}")
//                    val responseBody = String(error.networkResponse.data, charset("utf-8"))
//                    val data = JSONObject(responseBody)
//                    val message = data.optString("message")

                    }
                )
                // Add the request to the RequestQueue
                VolleySingleton.getInstance(this).add(jsonObjectRequest)
            }
        }





    }



    private fun checkOtpBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.otp_code, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        val ed1 = view.findViewById<EditText>(R.id.ED1)
        val ed2 = view.findViewById<EditText>(R.id.ED2)
        val ed3 = view.findViewById<EditText>(R.id.ED3)
        val ed4 = view.findViewById<EditText>(R.id.ED4)
        val PB = view.findViewById<ProgressBar>(R.id.PB)
        val TV = view.findViewById<TextView>(R.id.TV)
        val btn = view.findViewById<RelativeLayout>(R.id.continueBTN)
        ed1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (ed1.getText().toString().length == 1)
                {
                    ed2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        ed2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (ed1.getText().toString().length == 1)
                {
                    ed3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        ed3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (ed1.getText().toString().length == 1)
                {
                    ed4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        ed4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (ed4.getText().toString().length == 1)
                {

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        btn.setOnClickListener {

            PB.visibility = View.VISIBLE
            TV.visibility = View.GONE
            val jsonObject = JSONObject()
            jsonObject.put("otp",ed1.text.toString().trim()+ed2.text.toString().trim()+ed3.text.toString().trim()+ed4.text.toString().trim())
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                Constant.OTP_CODE,
                jsonObject,
                { response ->
                    Log.d("TAG", "checkOtpBottomSheet: ${response}")
                    PB.visibility = View.GONE
                    TV.visibility = View.VISIBLE
                    val success = response.getBoolean("success");
                    if(success == true){
                        val jsonObj = response.getJSONObject("message")
                        id = jsonObj.getString("_id")
                         resetBottomSheet()

                    }else{

                        val message = response.getString("message");
                        Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                    }

                },
                { error ->
                    println("response ${error.message}")
//                    val responseBody = String(error.networkResponse.data, charset("utf-8"))
//                    val data = JSONObject(responseBody)
//                    val message = data.optString("message")

                }
            )
            // Add the request to the RequestQueue
            VolleySingleton.getInstance(this).add(jsonObjectRequest)
        }
    }

    private fun resetBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.reset_password, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        val btn = view.findViewById<RelativeLayout>(R.id.resetBT)
        val ed1 = view.findViewById<EditText>(R.id.passED)
        val ed2 = view.findViewById<EditText>(R.id.conPassED)
        val PB = view.findViewById<ProgressBar>(R.id.PB)
        val TV = view.findViewById<TextView>(R.id.TV)
        btn.setOnClickListener {

            if (ed1.text.toString().trim() != ed2.text.toString().trim()) {
                ed1.error = "Check password fields"
                ed2.error = "Check confirm password fields"
            } else {
                PB.visibility = View.VISIBLE
                TV.visibility = View.GONE
                val jsonObject = JSONObject()
                jsonObject.put("password", ed1.text.toString().trim())
                jsonObject.put("id", id)
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST,
                    Constant.RESET_PASSWORD,
                    jsonObject,
                    { response ->

                        PB.visibility = View.GONE
                        TV.visibility = View.VISIBLE
                        val success = response.getBoolean("success")
                        if(success == true){
                            val message = response.getString("message")
                            Toast.makeText(applicationContext,"${message}", Toast.LENGTH_SHORT).show()

                        }else{
                            val message = response.getString("message");
                            Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
                        }

                    },
                    { error ->
                        println("response ${error.message}")
//                    val responseBody = String(error.networkResponse.data, charset("utf-8"))
//                    val data = JSONObject(responseBody)
//                    val message = data.optString("message")

                    }
                )
                // Add the request to the RequestQueue
                VolleySingleton.getInstance(this).add(jsonObjectRequest)
            }
        }





    }


}