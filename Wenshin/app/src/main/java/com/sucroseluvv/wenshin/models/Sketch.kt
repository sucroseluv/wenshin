package com.sucroseluvv.wenshin.models

data class Sketch(
    val id: Int,
    val image: String,
    val name:String,
    val description:String,
    val width: Double,
    val height: Double,
    val working_hours:Int,
    val is_public:Int,
    val author: Int,
    val tags: Array<String>?
)

fun getSketchHoursString (sketch: Sketch): String {
    val count = sketch.working_hours
    var hourText = "час"
    if(arrayOf(2,3,4,22,23,24).any { n -> n==count }) hourText = "часа"
    if(arrayOf(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,25,26,27,28,29,30).any { n -> n==count }) hourText = "часов"
    return "${sketch.working_hours} ${hourText}"
}