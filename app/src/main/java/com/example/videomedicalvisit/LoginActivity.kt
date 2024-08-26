package com.example.videomedicalvisit

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.videomedicalvisit.databinding.ActivityLoginBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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
}