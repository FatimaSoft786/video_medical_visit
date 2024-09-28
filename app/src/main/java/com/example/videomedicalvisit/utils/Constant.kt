package com.fatima.soft.dogz.utils

object Constant {
  var BASE_URL = "https://video-medico-backend-production.up.railway.app/";
   // var BASE_URL = "http://192.168.100.5:5000/"
    var LOGIN_API = BASE_URL + "api/user/login"
    var REGISTER_API = BASE_URL + "api/user/signup"
    var OTP_CODE = BASE_URL + "api/user/verifyOtp"
    var PROFILE = BASE_URL + "api/user/userDetails"
    var EDITPROFILE = BASE_URL + "api/user/editPatientProfile"
    var PATIENTMEDICALHISTORY = BASE_URL + "api/user/patientMedicalHistory"
    var DOCTORSLIST = BASE_URL + "api/user/doctorsList"
    var LIKE = BASE_URL + "api/user/like"
    var FAVORITEBYPATIENT = BASE_URL + "api/user/favoritesByPatient"
    var CHECKEMAIL = BASE_URL + "api/user/checkEmail"
    var RESET_PASSWORD = BASE_URL + "api/user/resetPassword"
    var UPLOADPICTURE = BASE_URL + "api/user/uploadProfilePicture"
    var DELETEFAVORITE = BASE_URL + "api/user/deleteFavoriteByPatient"
}