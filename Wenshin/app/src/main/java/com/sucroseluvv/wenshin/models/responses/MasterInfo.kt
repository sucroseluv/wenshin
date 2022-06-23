package com.sucroseluvv.wenshin.models.responses

data class MasterInfo(
    val name: String,
    val email: String,
    val phone: String,
    val avatar: String,
    val price: Float,
    val feedbacks: Array<FeedbackInfo>
)
