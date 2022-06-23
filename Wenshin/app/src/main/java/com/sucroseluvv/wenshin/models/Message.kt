package com.sucroseluvv.wenshin.models

import java.util.*

data class Message(val id: Int, val datetime: Date, val message: String, val author: String, val order_id: Int)
