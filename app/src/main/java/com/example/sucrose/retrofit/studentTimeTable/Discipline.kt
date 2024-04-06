package com.example.sucrose.retrofit.studentTimeTable

data class Discipline(
    val Auditorium: Auditorium,
    val Group: String,
    val Id: Int,
    val Language: Any,
    val LessonType: Int,
    val Remote: Boolean,
    val SubgroupNumber: Int,
    val Teacher: Teacher,
    val Title: String
)