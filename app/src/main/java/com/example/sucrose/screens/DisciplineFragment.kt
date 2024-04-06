package com.example.sucrose.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.Discipline_item
import com.example.sucrose.MyAdapter
import com.example.sucrose.GradeActivity
import com.example.sucrose.Retrofit.discepline.RecordBook
import com.example.sucrose.Retrofit.discepline.StudentSemester
import com.example.sucrose.Retrofit.studentSemester.StudentSemestrNoArgument
import com.example.sucrose.Retrofit.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.sucrose.Retrofit.studentSemester.StudentSemestrItem
import com.example.sucrose.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class DisciplineFragment : Fragment() {

    //Инициализация глобальных переменных
    lateinit var newRecycle: RecyclerView
    lateinit var full_duscipline: ArrayList<Discipline_item>
    private val retrofit = buildExRetrofit("https://papi.mrsu.ru/")
    private val userService = retrofit.create<UserService>(UserService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discipline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = getToken("private")//токен
        val authHeader = "Bearer $token" //Да
        val mainView = view //костыль чтобы не было ошикби по именам(!)

        val nobject_dissemestr = getStudentSemestrNoObjectFromSharedPreferences("Years_education")
        //здесь находится не только год,но и метка непрочитанных сообщений
        val items_perioddate = uniqueYearList(nobject_dissemestr)

        //println(items_perioddate)
        // текстовое поле с автозаполнением
        val autoComplete: AutoCompleteTextView = view.findViewById(R.id.autocompile_date_discipline)
        var adapterDatePeriod = ArrayAdapter(
            mainView.context,
            R.layout.list_item_dropdownmenu,
            items_perioddate
        )//адаптер для заполнения
        autoComplete.setAdapter(adapterDatePeriod)//устанавливаем адаптер


        var selectedYear: String =
            items_perioddate.last()//последний в списке(по умолчанию берем последний учебный год из полученных)
        var currentSem_num = 1//дефолтный 1 семестр

        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                selectedYear = parent.getItemAtPosition(position).toString()//получаем выбранный год
                responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
                //делаем новый запрос дисциплин по этому учебному году и семестру
                println(selectedYear)
                //println(items_perioddate)
            }

        //работа с кнопками(надо справить по возможности)
        val first_semestrbutton: Button = mainView.findViewById(R.id.first_semestrbutton)
        val second_semestrbutton: Button = mainView.findViewById(R.id.second_semestrbutton)
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
        //конец части с кнопками

        //(1)часть с отображаением и откликом
        responseDiscipline(authHeader, selectedYear, currentSem_num, mainView)
        //edge(1)
    }

    private fun uniqueYearList(nobject_dissemestr: List<StudentSemestrItem>?): ArrayList<String> {
        val items_year = ArrayList<String>()

        if (nobject_dissemestr != null && nobject_dissemestr.isNotEmpty()) {
            val items = ArrayList<String>()
            for (item in nobject_dissemestr) {
                items.add(item.Year)
            }
            items_year.addAll(items.distinct())
            println("========items_year $items_year")
        } else {
            // добавление дефолтного значения, если список пустой
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val str = "$currentYear - ${currentYear + 1}"
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
        //получаем дисциплины  и сразу вызваем адаптер отображаения(да,костыль)
        full_duscipline = arrayListOf()
        call.enqueue(object : Callback<StudentSemester> {
            override fun onResponse(
                call: Call<StudentSemester>,
                response: Response<StudentSemester>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()//data
                    if (data != null) {//not null
                        recyclerViewDiscipline(data, view)//отображаем дисциплины
                    } else {
                    }
                }
            }

            override fun onFailure(call: Call<StudentSemester>, t: Throwable) {
            }
        })
    }

    private fun recyclerViewDiscipline(data: StudentSemester, view: View) {
        val recordBook: List<RecordBook> = data.RecordBooks//records of discipline
        //функция отображаения дисциплин сама по себе огромный костыль
        //все отображение сделано через 1 Recyclerview(в разделе с расписанием сделано уже через вложенное)

        for (books in recordBook) {//проходимся по записям
            //имя факультета как маркер заголовка
            full_duscipline.add(Discipline_item(books.Faculty, true, 0))
            for (discipline in books.Disciplines) {
                full_duscipline.add(
                    Discipline_item(
                        discipline.Title,
                        false,
                        discipline.Id
                    )
                )//все остальные просто дисциплины
            }
        }

        newRecycle = view.findViewById(R.id.mRecyclerView)
        newRecycle.layoutManager = LinearLayoutManager(context)
        newRecycle.setHasFixedSize(true)

        var adapter = MyAdapter(full_duscipline)
        adapter.notifyDataSetChanged()//сообщаем адаптеру что набор данных изменился
        newRecycle.adapter = adapter

        adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {//считываем отклики по дисциплинам
                if (!full_duscipline[position].heading) {//если это не заголовок
                    val intent = Intent(context, GradeActivity::class.java)

                    intent.putExtra(
                        "name_discipline",
                        full_duscipline[position].text
                    )//имя дисциплины
                    saveToken(
                        full_duscipline[position].id.toString(),
                        "id_discipline"
                    )//id дисциплины
                    //запускаем новое активити чтобы перекрыть пред экран полность вместе с меню навигации
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