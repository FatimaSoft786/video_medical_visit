package com.example.videomedicalvisit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.videomedicalvisit.databinding.ActivityDoctorBinding

class DoctorActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}