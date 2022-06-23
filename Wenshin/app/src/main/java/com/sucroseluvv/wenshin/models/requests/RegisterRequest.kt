package com.sucroseluvv.wenshin.models.requests

data class RegisterRequest(val email: String, val password: String, val firstname: String, val lastname: String, val middlename: String, val phone: String)
