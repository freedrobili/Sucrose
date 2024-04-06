package com.example.sucrose.Retrofit.discepline

data class StudentSemester(
    val Period: Int,
    val RecordBooks: List<RecordBook>,
    val UnreadedDisCount: Int,
    val UnreadedDisMesCount: Int,
    val Year: String
)