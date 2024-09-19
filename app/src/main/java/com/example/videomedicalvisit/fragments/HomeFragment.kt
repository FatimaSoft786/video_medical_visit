package com.example.videomedicalvisit.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.Spec
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.videomedicalvisit.adapter.SpecialistAdapter
import com.example.videomedicalvisit.adapter.UserAdapter
import com.example.videomedicalvisit.databinding.FragmentHomeBinding
import com.example.videomedicalvisit.model.Specialist
import com.example.videomedicalvisit.model.User
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var token: String
    lateinit var sharedPref: SharedPreferences
    private var usersList = ArrayList<User>()
    private lateinit var adapter: UserAdapter
    private  var specialist = ArrayList<Specialist>()
    private lateinit var specialAdapter: SpecialistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref =  requireActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE)
        token = sharedPref.getString("token", "")!!
        val jsonObj = JSONObject()
        jsonObj.put("role","Doctor")

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.DOCTORSLIST,jsonObj,
            Response.Listener { response ->
                val array: JSONArray = response.getJSONArray("doctors_list")
                for (i in 0 until array.length()) {
                    val jsonOBJ = array.getJSONObject(i)
                    val user = User(jsonOBJ.getString("firstName"),
                        jsonOBJ.getString("lastName"),
                        jsonOBJ.getString("_id"),
                        jsonOBJ.getString("default_picture_url"),
                        jsonOBJ.getString("picture_url"),
                        jsonOBJ.getString("location"),
                        jsonOBJ.getString("specialist"))
                    usersList.add(user)
                   adapter = UserAdapter(requireActivity())
                    adapter.setListData(usersList)
                    binding.RV.adapter = adapter

                }
                println("response"+response)
//                binding.PB.visibility = View.GONE
//                binding.TV.visibility = View.VISIBLE
                val success = response.getBoolean("success")
//                if(success == true){
////                    Toast.makeText(applicationContext, "Dati salvati con successo!", Toast.LENGTH_SHORT).show()
//                }else{
//                    binding.PB.visibility = View.GONE
//                    binding.TV.visibility = View.VISIBLE
//                    val message = response.getString("message");
//                    Toast.makeText(applicationContext, "${message.toString()}", Toast.LENGTH_SHORT).show()
//                }
            },
            Response.ErrorListener { error ->
                // Handle error
//                binding.PB.visibility = View.GONE
//                binding.TV.visibility = View.VISIBLE
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
        VolleySingleton.getInstance(requireActivity()).add(jsonRequest)

        specialist.add(Specialist("Dermatologia"))
        specialist.add(Specialist("Cardiologia"))
        specialist.add(Specialist("Psicologia"))
        specialist.add(Specialist("Medicina dello Sport"))
        specialist.add(Specialist("Fisiatria"))
        specialist.add(Specialist("Medicina del Lavoro"))
        specialist.add(Specialist("Medico di famiglia"))
        specialAdapter = SpecialistAdapter(requireActivity())
        specialAdapter.setListData(specialist)
        binding.specialistRV.adapter = specialAdapter

        binding.search.setOnClickListener {

            val filteredByName = usersList.filter { user -> user.firstName!!.contains(binding.ed.text.toString().trim(), ignoreCase = true) }

          //  val filteredList = usersList.filter { it.firstName == binding.ed.text.toString().trim() }
            adapter.setListData(filteredByName.toMutableList())
            binding.RV.adapter = adapter
        }

    }


}