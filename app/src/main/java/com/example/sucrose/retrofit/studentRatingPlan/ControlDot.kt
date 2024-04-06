package com.example.sucrose.retrofit.studentRatingPlan

data class ControlDot(
    val createDate: String,
    val CreatorId: String,
    val Date: String,
    val Id: Int,
    val IsCredit: Boolean,
    val IsReport: Boolean,
    val Mark: Mark,
    val MaxBall: Double,
    val Order: Int,
    val Report: Report,
    val TestProfiles: List<Any>,
    val Title: String
)