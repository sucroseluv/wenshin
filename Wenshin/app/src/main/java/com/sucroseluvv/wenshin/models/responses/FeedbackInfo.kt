package com.sucroseluvv.wenshin.models.responses

data class FeedbackInfo(
    val sketchname: String,
    val hours: Int,
    val image: String,
    val name: String,
    val rate: Int,
    val comment: String
)
