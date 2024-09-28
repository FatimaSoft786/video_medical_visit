package com.example.videomedicalvisit.utils

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {
    @Multipart
    @POST("api/user/signup")
    fun registerDoctor(
        @Part cv: MultipartBody.Part,
        @Part("firstName") firstname: RequestBody?,
        @Part("lastName") lastname: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("sex") sex: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("postal_code") postalCode: RequestBody?,
        @Part("studies_start_year") start: RequestBody?,
        @Part("studies_end_year") end: RequestBody?,
        @Part("special_recognition") special: RequestBody?,
        @Part("specialist") specialist: RequestBody?,
        @Part("clinic_hospital_address") clinic: RequestBody?,
        @Part("about") about: RequestBody?,
        @Part("education") education: RequestBody?,
        @Part("role") role: RequestBody?,
        @Part("university") uni: RequestBody?,
        @Part("experience") exp: RequestBody?
    ): Call<DoctorRes>

    @Multipart
    @POST("api/user/uploadProfilePicture")
    fun uploadPicture(
        @Part profile: MultipartBody.Part,
        @Part("Id") id: RequestBody?,
        @Header("Authorization") token: String
    ):Call<DoctorRes>

}
