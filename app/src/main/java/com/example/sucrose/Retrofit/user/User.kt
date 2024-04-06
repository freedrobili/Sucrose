package com.example.sucrose.Retrofit.user

data class User(
    val AcademicDegree: Any,
    val AcademicRank: Any,
    val BirthDate: String,
    val Email: String,
    val EmailConfirmed: Boolean,
    val EnglishFIO: String,
    val FIO: String,
    val Id: String,
    val Photo: Photo,
    val Roles: List<Role>,
    val StudentCod: String,
    val TeacherCod: Any,
    val UserName: String
)