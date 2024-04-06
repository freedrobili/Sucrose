package com.example.sucrose.Retrofit.message

data class Response(
    val createDate: String,
    val Id: Int,
    val IsTeacher: Boolean,
    val Text: String,
    val User: User
)