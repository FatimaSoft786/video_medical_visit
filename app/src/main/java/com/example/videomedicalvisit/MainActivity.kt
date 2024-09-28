package com.example.videomedicalvisit


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.videomedicalvisit.databinding.ActivityMainBinding
import com.example.videomedicalvisit.fragments.AppointmentFragment
import com.example.videomedicalvisit.fragments.FavoriteFragment
import com.example.videomedicalvisit.fragments.HomeFragment
import com.example.videomedicalvisit.fragments.PaymentFragment
import com.example.videomedicalvisit.fragments.SettingsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val REQUEST_CODE = 1001
    private val STORAGE_PERMISSION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       permission()
        loadFragment(HomeFragment())


  binding.bottomNav.setOnItemSelectedListener {
      when(it.itemId){
          R.id.home -> {
              loadFragment(HomeFragment())
              true
          }
          R.id.favorite -> {
              loadFragment(FavoriteFragment())
              true
          }
          R.id.appointment -> {
              loadFragment(AppointmentFragment())
              true
          }
          R.id.wallet -> {
              loadFragment(PaymentFragment())
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

   private  fun permission(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
               != PackageManager.PERMISSION_GRANTED) {

               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                   101
               )
           }
       }
       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
           if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {

               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                   102
               )
           }
       }

   }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission for reading images is granted
                } else {
                   openAppSettings()
                }
            }
            102 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission for reading external storage is granted
                } else {
                    openAppSettings()
                }
            }
        }
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}