package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.retrofit.studentTimeTable.Lesson
import com.example.sucrose.retrofit.studentTimeTable.StudentTimeTableItem
import com.example.sucrose.R

class AdapterTimeTable(
    private val timeTable: ArrayList<StudentTimeTableItem>,
    private var groupNumber: Int = 1,
    private var TimeDiscipline: List<String>
) : RecyclerView.Adapter<AdapterTimeTable.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timetable_parent, parent, false)
        return MyViewHolder(itemView)
    }

    private fun parseLessons(lessons: List<Lesson>, subgroupnum: Int = 1): ArrayList<Lesson> {
        var newLessons = ArrayList<Lesson>()
        for (lesson in lessons) {
            if (lesson.SubgroupCount == 0) {
                newLessons.add(lesson)//общая лекция
                continue
            }
            var isexists: Boolean = false
            for (discipline in lesson.Disciplines) {
                if (discipline.SubgroupNumber == subgroupnum)
                    newLessons.add(lesson)
            }

        }
        return newLessons
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeTable[position]
        holder.clickker.setImageResource(R.drawable.keyboard_arrow_down)
        holder.Header_table.text = currentItem.FacultyName//имя факультета
        holder.recycler_timetable_child.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recycler_timetable_child.setHasFixedSize(true)


        val newLessons = parseLessons(
            currentItem.TimeTable.Lessons,
            groupNumber
        )//удаляем явнно ненужные предметы по подгруппе
        holder.recycler_timetable_child.adapter = AdaptTimeTableNested(
            newLessons,
            groupNumber,
            TimeDiscipline
        )//присваем адптеру свой адаптер

        if (currentItem.isExpandble) {//нажата ли кнопк
            holder.clickker.setImageResource(R.drawable.keyboard_arrow_up)//замена иконгки нажатия
            holder.relative_timetable_p.visibility = View.VISIBLE//сделать область видимой
        } else {
            holder.clickker.setImageResource(R.drawable.keyboard_arrow_down)
            holder.relative_timetable_p.visibility = View.GONE
        }

        holder.LinearLayout.setOnClickListener {//по нажатию творим грязь
            currentItem.isExpandble =
                !currentItem.isExpandble//по нажатию присваиваем противоположное значение
            notifyItemChanged(holder.adapterPosition)
            println("Click " + currentItem.FacultyName)

        }
    }

    override fun getItemCount(): Int {
        return timeTable.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var LinearLayout: LinearLayout = itemView.findViewById(R.id.linnear_item_timetable)
        var Header_table: TextView = itemView.findViewById(R.id.Header_table)

        var recycler_timetable_child: RecyclerView =
            itemView.findViewById(R.id.recycler_timetable_child)
        var relative_timetable_p: RelativeLayout = itemView.findViewById(R.id.relative_timetable_p)
        var clickker: ImageView = itemView.findViewById(R.id.clickker)
    }
}