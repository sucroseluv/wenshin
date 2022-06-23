package com.sucroseluvv.wenshin.models

enum class UserType {
    guest,
    user,
    master
}
data class UserInfo (val role: UserType, var token: String?)