package com.sucroseluvv.wenshin.models

import java.util.*

data class Master(val id: Int, val lastname: String, val firstname: String, val middlename: String, val avatar: String, val price: Float)

fun getMasterFio(master: Master): String {
    return "${master.lastname} ${master.firstname} ${master.middlename}"
}