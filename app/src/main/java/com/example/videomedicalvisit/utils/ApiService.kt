package com.example.videomedicalvisit.utils

import okhttp3.MultipartBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("api/user/uploadProfilePicture")
    fun uploadImage(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: String,
        @Part profile: MultipartBody.Part
    ): Call<ResponseBody>
}
