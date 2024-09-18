package com.example.videomedicalvisit

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        binding.signUp.setOnClickListener {
            startActivity(Intent(applicationContext,SignUpActivity::class.java))
            finish()
        }
        binding.forgotTV.setOnClickListener {
          checkEmailBottomSheet()
        }
    }
    private fun checkEmailBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.reset_password, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun otpCodeBottomSheet(){
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.check_email, null)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
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

                    binding.PB.visibility = View.GONE
                    binding.TV.visibility = View.VISIBLE
                    val success = response.getBoolean("success")
                    if(success == true){
                        val token = response.getString("accessToken")
                        val id = response.getString("id")
                        editor.putString("token",token)
                        editor.putString("id",id)
                        editor.apply()
                        startActivity(Intent(applicationContext,MainActivity::class.java))
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