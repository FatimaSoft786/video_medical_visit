package com.example.videomedicalvisit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.videomedicalvisit.databinding.FragmentEditDoctorProfileBinding


class EditDoctorProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditDoctorProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDoctorProfileBinding.inflate(layoutInflater)
        return binding.root
    }

}