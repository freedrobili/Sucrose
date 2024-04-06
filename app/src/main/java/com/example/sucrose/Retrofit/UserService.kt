package com.example.sucrose.Retrofit


import com.example.sucrose.Retrofit.discepline.StudentSemester
import com.example.sucrose.Retrofit.ForumMessage.ForumMessage
import com.example.sucrose.Retrofit.ForumMessage.ForumMessageItem
import com.example.sucrose.Retrofit.message.Message

import com.example.sucrose.Retrofit.studentRatingPlan.StudentRatingPlan
import com.example.sucrose.Retrofit.studentSemester.StudentSemestrNoArgument
import com.example.sucrose.Retrofit.studentTimeTable.StudentTimeTable
import com.example.sucrose.Retrofit.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("v1/User")
    fun getUser(
        @Header("Authorization") token: String
    ): retrofit2.Call<User>

    @GET("v1/StudentSemester")// Возвращает семестры где за студентом закреплены дисциплины (без объектов дисциплин +кол-во непрочитанных смс)
    fun getStudentSemestrNoObject(
        @Header("Authorization") token: String,
    ): retrofit2.Call<StudentSemestrNoArgument>

    @GET("v1/StudentSemester")// Возвращает заданный семестр со списком дисциплин
    fun getStudentSemestr(
        @Header("Authorization") token: String,
        @Query("year") year: String,
        @Query("period") period: Int
    ): retrofit2.Call<StudentSemester>

    @GET("v1/StudentRatingPlan/{id}")// Возвращает рейтинг-план для студента (с оценками и отчетами по контрольным точкам)
    fun getStudentRatingPlan(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): retrofit2.Call<StudentRatingPlan>

    @GET("v1/StudentTimeTable")//расписание по дате
    fun getStudentTimeTable(
        @Header("Authorization") token: String,
        @Query("date") date: String,
    ): retrofit2.Call<StudentTimeTable>

    @GET("v1/ForumMessage")//получение сообщений по дисциплине
    fun getForumMessage(
        @Header("Authorization") token: String,
        @Query("disciplineId") disciplineId: Int,
        @Query("count") count: Int
    ): retrofit2.Call<ForumMessage>

    @POST("v1/ForumMessage")//отправка сообщения
    fun sendMessage(
        @Header("Authorization") token: String,
        @Query("disciplineId") disciplineId: String,
        @Body message: Message
    ): Call<ForumMessageItem>

    @DELETE("v1/ForumMessage/{id}")//удалить сообщение по id
    fun deleteMessage(
        @Header("Authorization") token: String,
        @Path("id") messageId: String
    ): Call<Void>
}