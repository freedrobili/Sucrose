package com.example.sucrose.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.ChatActivity
import com.example.sucrose.Discipline_item
import com.example.sucrose.MyAdapter
import com.example.sucrose.R
import com.example.sucrose.Retrofit.discepline.RecordBook
import com.example.sucrose.Retrofit.discepline.StudentSemester
import com.example.sucrose.Retrofit.studentSemester.StudentSemestrItem
import com.example.sucrose.Retrofit.studentSemester.StudentSemestrNoArgument
import com.example.sucrose.Retrofit.UserService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class TalkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talk, container, false)
    }

    lateinit var newRecycle: RecyclerView
    lateinit var full_duscipline: ArrayList<Discipline_item>
    private val retrofit = buildExRetrofit("https://papi.mrsu.ru/")
    private val userService = retrofit.create<UserService>(UserService::class.java)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = getToken("private")
        val authHeader = "Bearer " + token;
        val mainView = view

        val nobject_dissemestr =
            getStudentSemestrNoObjectFromSharedPreferences("Years_education")//здесь находится не только год нол и метка непрочитанных сообщений
        val items_perioddate = uniqueYearList(nobject_dissemestr)
        println(items_perioddate)
        val autoComplete: AutoCompleteTextView = view.findViewById(R.id.autocompile_date_talk)

        var adapterDatePeriod =
            ArrayAdapter(mainView.context, R.layout.list_item_dropdownmenu, items_perioddate)
        autoComplete.setAdapter(adapterDatePeriod)


        var selectedYear: String = items_perioddate.last()//последний в списке
        var currentSem_num: Int = 1
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                selectedYear = parent.getItemAtPosition(position).toString()

                responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
                println(selectedYear)
                println(items_perioddate)
            }
        val first_semestrbutton: Button = mainView.findViewById(R.id.first_semestrbutton_talk)
        val second_semestrbutton: Button = mainView.findViewById(R.id.second_semestrbutton_talk)
        val light_purple = ContextCompat.getColor(view.context, R.color.light_purple)
        val purple = ContextCompat.getColor(view.context, R.color.colorPrimary)

        first_semestrbutton.setBackgroundColor(light_purple)

        first_semestrbutton.setOnClickListener {
            currentSem_num = 1
            first_semestrbutton.setBackgroundColor(light_purple)
            second_semestrbutton.setBackgroundColor(purple)
            responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
        }

        second_semestrbutton.setOnClickListener {
            currentSem_num = 2
            first_semestrbutton.setBackgroundColor(purple)
            second_semestrbutton.setBackgroundColor(light_purple)
            responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
        }
        //(1)часть с отображаением и откликом
        responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
        //edge(1)
    }

    private fun uniqueYearList(nobject_dissemestr: List<StudentSemestrItem>?): ArrayList<String> {
        var items_year = ArrayList<String>()
        var str: String = ""
        if (nobject_dissemestr != null) {
            val items = ArrayList<String>()
            for (item in nobject_dissemestr) {
                items.add(item.Year)
            }
            items_year = items.distinct() as ArrayList<String>
        } else {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            str = currentYear.toString() + " - " + (currentYear + 1).toString()
            items_year.add(str)
            println("Ошибка при получении дат")
        }
        return items_year
    }

    private fun responseDiscipline(
        authHeader: String,
        selectedYear: String,
        semestr_num: Int,
        view: View
    ) {
        val call = userService.getStudentSemestr(authHeader, selectedYear, semestr_num)
        full_duscipline = arrayListOf()
        call.enqueue(object : Callback<StudentSemester> {
            override fun onResponse(
                call: Call<StudentSemester>,
                response: Response<StudentSemester>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()//data
                    if (data != null) {//not null
                        recyclerViewDiscipline(data, view)
                    }
                }
            }

            override fun onFailure(call: Call<StudentSemester>, t: Throwable) {
            }
        })
    }

    private fun recyclerViewDiscipline(data: StudentSemester, view: View) {
        val recordBook: List<RecordBook> = data.RecordBooks//records of discipline
        for (books in recordBook) {//items contains headings
            full_duscipline.add(Discipline_item(books.Faculty, true, 0))
            for (discipline in books.Disciplines) {
                full_duscipline.add(Discipline_item(discipline.Title, false, discipline.Id))
            }
        }

        newRecycle = view.findViewById(R.id.mRecyclerView_talk)
        newRecycle.layoutManager = LinearLayoutManager(context)
        newRecycle.setHasFixedSize(true)
        val adapter = MyAdapter(full_duscipline)
        adapter.notifyDataSetChanged()
        newRecycle.adapter = adapter

        adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (!full_duscipline[position].heading) {//если это не заголовок
                    //переход на активити общения
                    val intent = Intent(context, ChatActivity::class.java)
                    println("ID discipline from CHat: " + full_duscipline[position].id)
                    intent.putExtra("name_discipline_forChat", full_duscipline[position].text)
                    intent.putExtra(
                        "id_discipline_forChat",
                        full_duscipline[position].id.toString()
                    )

//                    saveToken(full_duscipline[position].id.toString(), "id_discipline")
                    startActivity(intent)
                }
            }
        })
    }

    private fun getStudentSemestrNoObjectFromSharedPreferences(sharedKey: String): List<StudentSemestrItem>? {
        val sharedPreferences =
            requireActivity().getSharedPreferences("myAppPrefSemestNoOb", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(sharedKey, null)
        val type = object : TypeToken<StudentSemestrNoArgument>() {}.type
        val data = if (!json.isNullOrEmpty()) Gson().fromJson<StudentSemestrNoArgument>(
            json,
            type
        ) else emptyList()
        return data
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
