package com.sucroseluvv.wenshin.models.responses

data class MasterShortOrderInfo(
    val id: Int,
    val image: String,
    val name: String,
    val clientname: String,
    val masterId: Int?
)
