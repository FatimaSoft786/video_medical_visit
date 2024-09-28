package com.example.videomedicalvisit

import android.content.Intent
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.videomedicalvisit.databinding.ActivitySignUpBinding
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import java.nio.file.Files.size


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.showIV.setOnClickListener {
            if (isPasswordVisible) {
                // Hide password
                binding.passED.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showIV.setImageResource(R.drawable.pass_visible)
            } else {
                // Show password
                binding.passED.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showIV.setImageResource(R.drawable.baseline_remove_red_eye_24)
            }
            // Move the cursor to the end of the text
            binding.passED.setSelection(binding.passED.text.length)
            isPasswordVisible = !isPasswordVisible
        }
        binding.signUp.setOnClickListener {
        signUp()
        }
        binding.privacyTV.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://res.cloudinary.com/dm5cvivrc/image/upload/v1716117064/Informativa_sulla_Privacy_gsajxd.pdf"))
            startActivity(intent)
        }
        binding.login.setOnClickListener {
            startActivity(Intent(applicationContext,LoginActivity::class.java))
        }
        binding.termsCondition.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://res.cloudinary.com/dm5cvivrc/image/upload/v1716117500/ITA_Termini_e_Condizioni_rbomze.pdf"))
            startActivity(intent)
        }
    }
    private fun signUp() {
        val emailAddress = binding.emailED.text.toString().trim()
        if(binding.firstED.text.toString().isEmpty()){
            binding.firstED.error = "Please enter first name."
        }else if(binding.lastED.text.toString().isEmpty()){
            binding.lastED.error = "Please enter last name."
        } else if (binding.emailED.text.toString().isEmpty()) {
            binding.emailED.error = "Please enter email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            binding.emailED.error = "Email is Invalid"
        } else if (binding.passED.text.toString().isEmpty()) {
            binding.passED.error = "Please enter password"
        } else if (binding.passED.text.toString().length < 6) {
            binding.passED.error = "Password length must be 6 characters"
        } else if(binding.mobile.text.toString().length < 11){
            binding.mobile.error = "Please enter mobile number"
        } else {
            binding.PB.visibility = View.VISIBLE
            binding.TV.visibility = View.GONE
            val jsonObject = JSONObject()
            jsonObject.put("email", binding.emailED.text.toString().trim())
            jsonObject.put("password",binding.passED.text.toString().trim())
            jsonObject.put("phoneNumber", binding.mobile.text.toString().trim())
            jsonObject.put("lastName", binding.lastED.text.toString().trim())
            jsonObject.put("firstName", binding.firstED.text.toString().trim())
            jsonObject.put("role","Patient")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                Constant.REGISTER_API,
                jsonObject,
                { response ->
                    Log.d("TAG", "signUp: ${response.toString()}")
                    binding.PB.visibility = View.GONE
                    binding.TV.visibility = View.VISIBLE
                    val success = response.getBoolean("success");
                    if(success == true){
                        Log.d("TAG", "signUp: ${success}")
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
                    PB.visibility = View.GONE
                    TV.visibility = View.VISIBLE
                    val success = response.getBoolean("success");
                    if(success == true){
                        Toast.makeText(applicationContext, "Codice OTP verificato con successo", Toast.LENGTH_SHORT).show()
                       startActivity(Intent(applicationContext,LoginActivity::class.java))
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