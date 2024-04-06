package com.example.sucrose.Retrofit.studentRatingPlan

data class Section(
    val ControlDots: List<ControlDot>,
    val createDate: String,
    val CreatorId: String,
    val Description: Any,
    val Id: Int,
    val Order: Int,
    val SectionType: Int,
    val Title: String
)