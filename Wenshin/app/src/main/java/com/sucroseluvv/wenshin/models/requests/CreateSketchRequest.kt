package com.sucroseluvv.wenshin.models.requests

data class CreateSketchRequest(
    val image: String,
    val name: String,
    val description: String,
    val width: Float,
    val height: Float,
    val workingHours: Int,
    val tags: Array<Int>
)
