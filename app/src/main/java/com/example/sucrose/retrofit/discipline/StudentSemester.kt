package com.example.sucrose.retrofit.discipline

data class StudentSemester(
    val Period: Int,
    val RecordBooks: List<RecordBook>,
    val UnreadedDisCount: Int,
    val UnreadedDisMesCount: Int,
    val Year: String
)