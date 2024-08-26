package com.example.videomedicalvisit


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.videomedicalvisit.databinding.ActivityMainBinding
import com.example.videomedicalvisit.fragments.HomeFragment
import com.example.videomedicalvisit.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

  binding.bottomNav.setOnItemSelectedListener {
      when(it.itemId){
          R.id.home -> {
              loadFragment(HomeFragment())
              true
          }
          R.id.favorite -> {
              loadFragment(HomeFragment())
              true
          }
          R.id.appointment -> {
              loadFragment(HomeFragment())
              true
          }
          R.id.wallet -> {
              loadFragment(HomeFragment())
              true
          }
          R.id.settings -> {
              loadFragment(SettingsFragment())
              true
          }
          else -> {
              false
          }
      }
  }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}