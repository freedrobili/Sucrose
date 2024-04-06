package com.example.sucrose.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sucrose.R
import com.example.sucrose.retrofit.studentSemester.StudentSemestrNoArgument
import com.example.sucrose.retrofit.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private val retrofit = buildExRetrofit("https://papi.mrsu.ru/")//базовая url часть для отправки
    private val userService =
        retrofit.create(UserService::class.java)// создаем retrofit на базе нашего интерфейса

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = getToken("private")//получен токен по ключу из sharedPreference
        val authHeader = "Bearer $token"
        getStudentSemestrNoObjectDiscipline(authHeader)//получаем данные по периодам раньше навигации(хотя смсыл если вызов асинхронный)

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)//находим id нижнего меню
        val navController = (childFragmentManager
            .findFragmentById(R.id.main_frag_conteiner) as NavHostFragment)
            .navController //извлекаем navcontroller для управления навигацией
        NavigationUI.setupWithNavController(
            bottomNavigationView,
            navController
        )//связываем контоллер и нижнее меню
    }

    private fun getStudentSemestrNoObjectDiscipline(authHeader: String) {
        val call_semestr_years =
            userService.getStudentSemestrNoObject(authHeader)//доступные периоды для показа дисциплин
        //получаем не только период с семестром но и метками непрочитанных сообщений

        call_semestr_years.enqueue(object : Callback<StudentSemestrNoArgument> {
            // (для асинхронного вызова)
            override fun onResponse(
                call: Call<StudentSemestrNoArgument>,
                response: Response<StudentSemestrNoArgument>
            ) {
                if (response.isSuccessful) {
                    val data_years = response.body()
                    if (data_years != null) {
                        saveStudentSemestrNoObjectToSharedPreferences(data_years, "Years_education")
                        //сохраняем данные периодов семетсрои и не проч сообщений
                    }
                } else {
                    println("Ошибка в main фрагмент response")//для отладки
                }
            }

            override fun onFailure(call: Call<StudentSemestrNoArgument>, t: Throwable) {
                print("Ошибка в main фрагмент onFailure")//для отладки
            }
        })
    }

    private fun saveStudentSemestrNoObjectToSharedPreferences(
        myDataList: StudentSemestrNoArgument,
        keyShared: String
    ) {
        val gson = Gson()
        val json = gson.toJson(myDataList)//перегнать в json
        val sharedPreferences =
            requireActivity().getSharedPreferences("myAppPrefSemestNoOb", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(keyShared, json).apply()
    }

    private fun getToken(key: String): String {
        val sharedPref = requireActivity().getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val token_l = sharedPref.getString(key, null).toString()
        return token_l
    }

    private fun saveToken(token: String, key: String) {
        val sharedPref = requireActivity().getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, token)
        editor.apply()
    }

    private fun buildExRetrofit(baseurl: String): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit
    }
}