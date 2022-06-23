package com.sucroseluvv.wenshin.models.responses

data class UserHistoryInfoResponse(val id: Int?, val status: String, val sketch_id: Int,
                                   val client_id: Int, val master_id: Int, val feedbackId: Int?,
                                   val rate: Int?, val comment: String?, val image: String, val working_hours: Int,
                                   val name: String, val masterName: String, val avatar: String, val price: Float, val amount: Float)
