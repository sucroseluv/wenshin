package com.sucroseluvv.wenshin.models.requests

data class SendFeedbackRequest(val orderId: Int, val comment: String?, val rate: Int)
