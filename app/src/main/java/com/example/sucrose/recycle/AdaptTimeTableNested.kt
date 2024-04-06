package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.Retrofit.studentTimeTable.Lesson
import com.example.sucrose.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdaptTimeTableNested(
    private val lessons: List<Lesson>,
    private var groupNumber: Int = 1,
    private var TimeDiscipline: List<String>
) : RecyclerView.Adapter<AdaptTimeTableNested.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.nested_item_timetable_rv, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = lessons[position]//конкретный элемент дисциплины

        var index_discipline = 0
        for (i in currentItem.Disciplines.indices) {
            if (currentItem.Disciplines[i].SubgroupNumber == groupNumber)
                index_discipline = i//ищем инедкс с нашей подгруппой
        }

        holder.texthed.text = currentItem.Disciplines[index_discipline].Title
        var auditorium = currentItem.Disciplines[index_discipline].Auditorium
        if (auditorium.Title != null) {//если не дистант
            holder.texttime_nested_timetable.text =
                TimeDiscipline[currentItem.Number - 1] + "[K." + auditorium.CampusTitle + " " + auditorium.Number + "]"
        } else {//если это дистант
            holder.texttime_nested_timetable.text =
                TimeDiscipline[currentItem.Number - 1]//если дистант добавляем значек дистанта
        }
        holder.username_nested_timetable.text =
            currentItem.Disciplines[index_discipline].Teacher.UserName
        var url_image_username =
            currentItem.Disciplines[index_discipline].Teacher.Photo.UrlMedium//загрузка фото преподавателя

        Picasso.get().load(url_image_username)
            .placeholder(R.drawable.unknown_user)// Временное изображение, показываемое во время загрузки
            .error(R.drawable.unknown_user) // Изображение, отображаемое в случае ошибки загрузки
            .into(holder.circleimage_nested_timetable)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texthed: TextView = itemView.findViewById(R.id.texthed_nested_timetable)
        val texttime_nested_timetable: TextView =
            itemView.findViewById(R.id.texttime_nested_timetable)
        val username_nested_timetable: TextView =
            itemView.findViewById(R.id.username_nested_timetable)
        val circleimage_nested_timetable: CircleImageView =
            itemView.findViewById(R.id.circleimage_nested_timetable)
    }
}