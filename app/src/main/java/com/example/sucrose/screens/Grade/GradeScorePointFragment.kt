package com.example.sucrose.screens.Grade

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.recycle.AdapterRatingScoreParent
import com.example.sucrose.recycle.СontrolDotView
import com.example.sucrose.Retrofit.studentRatingPlan.Section
import com.example.sucrose.Retrofit.studentRatingPlan.StudentRatingPlan
import com.example.sucrose.Retrofit.UserService
import com.example.sucrose.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GradeScorePointFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grade_score_point, container, false)
    }

    lateinit var view_controlpoint: ArrayList<СontrolDotView>
    lateinit var newRecycle: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newRecycle = view.findViewById(R.id.RatingScoreRecycler_parent)
        val score_gradefragment: TextView = view.findViewById(R.id.score_gradefragment)
        newRecycle.layoutManager = LinearLayoutManager(context)
        newRecycle.setHasFixedSize(true)

        val id_discipline = getToken("id_discipline")//id дисциплины
        println("ID_DISCIPLINE " + id_discipline)

        val token = getToken("private")//токен авторизации
        val retrofit = buildExRetrofit("https://papi.mrsu.ru/")//базовый запрос
        val userService = retrofit.create<UserService>(UserService::class.java)//интерфейс
        val authHeader = "Bearer " + token;//ДА
        val call =
            userService.getStudentRatingPlan(authHeader, id_discipline)//токен и айди дисциплины


        view_controlpoint = arrayListOf<СontrolDotView>()

        call.enqueue(object : Callback<StudentRatingPlan> {
            override fun onResponse(
                call: Call<StudentRatingPlan>,
                response: Response<StudentRatingPlan>
            ) {
                if (response.isSuccessful) {
                    val StudentRatingPlan =
                        response.body()//состоит из MarMarkZeroSession и Sections
                    if (StudentRatingPlan != null) {
                        var DataScore: List<Section> =
                            StudentRatingPlan.Sections//секции с заголовокм и частями заголовка(контрольными точками)
                        newRecycle.adapter = AdapterRatingScoreParent(DataScore)
                        var total_ball = parseControlPoint(StudentRatingPlan).toString()
                        score_gradefragment.text = "Всего: $total_ball" + "/100"


                        print("Point " + view_controlpoint)
                    }
                    println("SUCCESSFUL StusentRatingPlan")
                } else {

                }
            }

            override fun onFailure(call: Call<StudentRatingPlan>, t: Throwable) {
                TODO("Not yet implemented")
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

    private fun parseControlPoint(StudentRatingPlan: StudentRatingPlan): Double {

        //часть из этого не используется(связи с изменениями отрисовки)
        var view_controlpoint: ArrayList<СontrolDotView>
        view_controlpoint = arrayListOf<СontrolDotView>()
        val MarkZeroSession = StudentRatingPlan.MarkZeroSession//оценка за нулевку если она есть
        //обработка данных для RecuclerView
        var Sections: List<Section> = StudentRatingPlan.Sections//секции по контрольным точкам

        var sumball: Double = 0.0
        for (section in Sections) {
            val conttrolDots = section.ControlDots//контрольная точка
            val heading =
                if (section.Title != null) section.Title else "-"//спс тем кто ставит null в строке
            //view_controlpoint.add(СontrolDotView(heading,true,0.0,0.0))//заголовок
            for (controlDot in conttrolDots) {
                val maxball = if (controlDot.MaxBall != null) controlDot.MaxBall else 0.0
                val ball = if (controlDot.Mark != null) controlDot.Mark.Ball else 0.0
                sumball += ball
                val title = if (controlDot.Title != null) controlDot.Title else "-"
//                view_controlpoint.add(СontrolDotView(
//                    title,
//                    false,
//                    ball,
//                    maxball))
            }
        }
        return sumball
    }


}
