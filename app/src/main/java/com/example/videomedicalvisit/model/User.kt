package com.example.videomedicalvisit.model

data class User(
    val firstName: String? = "",
    val lastName: String? = "", val _id: String? = "",
    val default_picture_url: String? = "", val picture_url: String? = "", val location: String? = "", val specialist: String?="", val favorites: String
)