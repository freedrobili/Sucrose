package com.example.sucrose.Retrofit.ForumMessage

data class ForumMessageItem(
    val CreateDate: String,
    val Id: Int,
    val IsTeacher: Boolean,
    val Text: String,
    val User: User
)