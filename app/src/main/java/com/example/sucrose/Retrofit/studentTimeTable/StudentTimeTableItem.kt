package com.example.sucrose.Retrofit.studentTimeTable

data class StudentTimeTableItem(
    val FacultyName: String,
    val Group: String,
    var isExpandble: Boolean = false,
    val PlanNumber: String,
    val TimeTable: TimeTable,
    val TimeTableBlockd: Int
)