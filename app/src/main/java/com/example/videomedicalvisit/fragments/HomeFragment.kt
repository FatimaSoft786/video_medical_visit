package com.example.videomedicalvisit.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.videomedicalvisit.R
import com.example.videomedicalvisit.adapter.SearchResultAdapter
import com.example.videomedicalvisit.adapter.SpecialistAdapter
import com.example.videomedicalvisit.adapter.UserAdapter
import com.example.videomedicalvisit.databinding.FragmentHomeBinding
import com.example.videomedicalvisit.model.Specialist
import com.example.videomedicalvisit.model.User
import com.fatima.soft.dogz.utils.Constant
import com.fatima.soft.dogz.utils.VolleySingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment(), SpecialistAdapter.OnCategoryClickListener {
    private lateinit var binding: FragmentHomeBinding
    lateinit var token: String
    lateinit var sharedPref: SharedPreferences
    private var usersList = ArrayList<User>()
    private lateinit var adapter: UserAdapter
    private  var specialist = ArrayList<Specialist>()
    private lateinit var specialAdapter: SpecialistAdapter
    private var job: Job? = null
    private var searchResults = ArrayList<User>()
    private lateinit var searchAdapter: SearchResultAdapter
    private lateinit var popupWindow: PopupWindow
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref =  mContext!!.getSharedPreferences("MyPreferences", MODE_PRIVATE)
        token = sharedPref.getString("token", "")!!
        getProfile()
        getDoctors()

        specialist.add(Specialist("All"))
        specialist.add(Specialist("Dermatologia"))
        specialist.add(Specialist("Cardiologia"))
        specialist.add(Specialist("Psicologia"))
        specialist.add(Specialist("Medicina dello Sport"))
        specialist.add(Specialist("Fisiatria"))
        specialist.add(Specialist("Medicina del Lavoro"))
        specialist.add(Specialist("Medico di famiglia"))
        specialAdapter = SpecialistAdapter(mContext!!,this)
        specialAdapter.setListData(specialist)
        binding.specialistRV.adapter = specialAdapter


        searchAdapter = SearchResultAdapter(mContext!!)
        searchAdapter.setListData(searchResults)
        setupPopupWindow()
        binding.search.setOnClickListener {

            val filteredResults = searchResults.filter { s->
                s.specialist!!.contains(binding.ed.text.toString().trim(), ignoreCase = true) ||
                        s.firstName!!.contains(binding.ed.text.toString().trim(), ignoreCase = true) ||
                        s.lastName!!.contains(binding.ed.text.toString().trim(), ignoreCase = true)
            }
            searchAdapter.setListData(filteredResults.toMutableList())
            showPopupWindow(binding.search)
            binding.ed.text = null

        }
//        binding.ed.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                job?.cancel()
//                job = CoroutineScope(Dispatchers.Main).launch {
//                    delay(300) // Debounce time, e.g., 300ms
//                    val filteredResults = searchResults.filter {
//                        it.specialist!!.contains(s.toString(), ignoreCase = true) ||
//                                it.firstName!!.contains(s.toString(), ignoreCase = true) ||
//                                it.lastName!!.contains(s.toString(), ignoreCase = true)
//                    }
//                    searchAdapter.setListData(filteredResults.toMutableList())
//                    showPopupWindow(binding.ed)
//                }
//
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        })

    }

    private fun getProfile(){
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,Constant.PROFILE, null,
            Response.Listener { response ->
               try {
                   val success = response.getBoolean("success")
                   if(success == true){
                       val user_details = response.getJSONObject("user_details")
                       binding.nameTV.text = getString(R.string.hi) + user_details.getString("firstName") +" "+ user_details.getString("lastName")
                   }else{
                       val message = response.getString("message");
                       Toast.makeText(mContext!!, "${message.toString()}", Toast.LENGTH_SHORT).show()
                   }
               }catch (e: Exception){
                   e.printStackTrace()
               }
            },
            Response.ErrorListener { error ->
                // Handle error
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
        VolleySingleton.getInstance(mContext!!).add(jsonRequest)
    }
    private fun setupPopupWindow() {
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_search_layout, null)
       val recyclerView = popupView.findViewById<RecyclerView>(R.id.RV)
        recyclerView.layoutManager = LinearLayoutManager(mContext!!)
        recyclerView.adapter = searchAdapter


        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,

            true
        )
        popupWindow.elevation = 20F


    }

    private fun showPopupWindow(anchorView: View) {
        popupWindow.showAsDropDown(anchorView, 10, 20)
    }
    private  fun getDoctors(){
        val jsonObj = JSONObject()
        jsonObj.put("role","Doctor")
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, Constant.DOCTORSLIST,jsonObj,
            Response.Listener { response ->
              try {
                  usersList.clear()
                  val array: JSONArray = response.getJSONArray("doctors_list")
                  for (i in 0 until array.length()) {
                      val jsonOBJ = array.getJSONObject(i)
                      val user = User(jsonOBJ.getString("firstName"),
                          jsonOBJ.getString("lastName"),
                          jsonOBJ.getString("_id"),
                          jsonOBJ.getString("default_picture_url"),
                          jsonOBJ.getString("picture_url"),
                          jsonOBJ.getString("location"),
                          jsonOBJ.getString("specialist"),
                          jsonOBJ.getString("favorites"))
                      usersList.add(user)
                      searchResults.add(user)
                      adapter = UserAdapter(mContext!!)
                      adapter.setListData(usersList)
                      binding.RV.adapter = adapter

                  }
              }catch (e: Exception){
                  e.printStackTrace()
              }

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
    }

    override fun onCategoryClick(specialist: Specialist, position: Int) {
        if(specialist.specialist == "All"){
          return  getDoctors()
        }
        val filteredList = usersList.filter { it.specialist == specialist.specialist }
        adapter.setListData(filteredList.toMutableList())
        binding.RV.adapter = adapter
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}