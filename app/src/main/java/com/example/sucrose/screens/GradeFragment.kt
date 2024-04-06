package com.example.sucrose.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sucrose.R
import com.example.sucrose.Retrofit.user.User
import com.example.sucrose.Retrofit.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GradeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grade, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = getToken("private")

        val retrofit = buildExRetrofit("https://papi.mrsu.ru/")
        val userService = retrofit.create(UserService::class.java)
        val authHeader = "Bearer $token"

        val call = userService.getUser(authHeader)
        val fio_student: TextView = view.findViewById(R.id.Fio_student)
        val uid_student: TextView = view.findViewById(R.id.uid_student)
//        val birthday: TextView = view.findViewById(R.id.)
        call.enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
//                        birthday
                        fio_student.text = user.FIO
                        uid_student.text = "ID: " + user.StudentCod
                    }
                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })

    }

    private fun getToken(key: String): String {
        val sharedPref = requireActivity().getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val token_l = sharedPref.getString(key, null).toString()
        return token_l
    }

    private fun buildExRetrofit(baseurl: String): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit
    }
}