package com.fatima.soft.dogz.utils

object Constant {
    var BASE_URL = "https://video-medico-backend-production.up.railway.app/";
    var LOGIN_API = BASE_URL + "api/user/login"
    var REGISTER_API = BASE_URL + "api/user/signup"
    var OTP_CODE = BASE_URL + "api/user/verifyOtp"
    var PROFILE = BASE_URL + "api/user/userDetails"
    var EDITPROFILE = BASE_URL + "api/user/editPatientProfile"
    var PATIENTMEDICALHISTORY = BASE_URL + "api/user/patientMedicalHistory"
}