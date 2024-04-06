package com.example.sucrose.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.R
import com.example.sucrose.recycle.AdapterTimeTable
import com.example.sucrose.Retrofit.studentTimeTable.StudentTimeTable
import com.example.sucrose.Retrofit.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class TableFragment : Fragment() {

    lateinit var timeTable: StudentTimeTable
    lateinit var recycler: RecyclerView
    lateinit var table_req: StudentTimeTable
    private val retrofit = buildExRetrofit("https://papi.mrsu.ru/")
    private val userService = retrofit.create(UserService::class.java)
    private var currenSubGroup: Int = 1//текущая подгруппа
    private val TimeDiscipline = listOf(
        "1. 8:30 - 10:00",
        "2. 10:10 - 11:40",
        "3. 12:00 - 13:30",
        "4. 13:45 - 15:15",
        "5. 15:25 - 15:55",
        "6. 17:05 - 18:35",
        "7. 18:40 - 20:10"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //INITIALIZATION
        val first_subgroup = view.findViewById<Button>(R.id.first_semest_timetable)
        val second_subgroup = view.findViewById<Button>(R.id.second_semest_timetable)
        val calendarView: CalendarView = view.findViewById(R.id.calendarview)
        val token = getToken("private")
        val authHeader = "Bearer $token"
        recycler = view.findViewById(R.id.recycler_timetable_parent)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        val currentDate = getCurentDate()//текущая дата
        updatingResponseDateTime(currentDate, authHeader, currenSubGroup)

        val light_purple = ContextCompat.getColor(view.context, R.color.light_purple)
        val purple = ContextCompat.getColor(view.context, R.color.colorPrimary)

        first_subgroup.setBackgroundColor(light_purple)

        first_subgroup.setOnClickListener {
            first_subgroup.setBackgroundColor(light_purple)
            second_subgroup.setBackgroundColor(purple)
            currenSubGroup = 1
            if (table_req != null)
                recycler.adapter = AdapterTimeTable(table_req, 1, TimeDiscipline)
        }
        second_subgroup.setOnClickListener {
            first_subgroup.setBackgroundColor(purple)
            second_subgroup.setBackgroundColor(light_purple)
            currenSubGroup = 2
            if (table_req != null)
                recycler.adapter = AdapterTimeTable(table_req, 2, TimeDiscipline)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            println("$year $month $dayOfMonth")
            val date = "$year-${month + 1}-$dayOfMonth"
            updatingResponseDateTime(date, authHeader, currenSubGroup)
        }
    }

    private fun getCurentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month =
            (calendar.get(Calendar.MONTH) + 1).toString() // Добавляем 1, так как January имеет значение 0
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        return "${year}-${month}-${day}"
    }

    private fun updatingResponseDateTime(
        date: String,
        authHeader: String,
        subgroup: Int = 1
    ): StudentTimeTable {
        val call = userService.getStudentTimeTable(authHeader, date)
        var requer = StudentTimeTable()
        call.enqueue(object : Callback<StudentTimeTable> {
            override fun onResponse(
                call: Call<StudentTimeTable>,
                response: Response<StudentTimeTable>
            ) {
                if (response.isSuccessful) {
                    requer = response.body()!!//данные
                    if (requer != null) {
                        for (sect in requer) {
                            sect.isExpandble =
                                false//привинтивно устанавливаем не раскрытые recyclerview
                        }
                        table_req = requer
                        recycler.adapter = AdapterTimeTable(requer, subgroup, TimeDiscipline)
                    }
                    //println("Успех")
                } else {
                    println("Не правильно,широкую на широкую!!!")
                }
            }

            override fun onFailure(call: Call<StudentTimeTable>, t: Throwable) {
                TODO("Not yet implemented")
                //  requer.add(item)
                println("Пум пум пум")
            }
        })
        return requer
    }


    private fun buildExRetrofit(baseurl: String): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit
    }

    private fun getToken(key: String): String {
        val sharedPref = requireActivity().getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val token_l = sharedPref.getString(key, null).toString()
        return token_l
    }


}


