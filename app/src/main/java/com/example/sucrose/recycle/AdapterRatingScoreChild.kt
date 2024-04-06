package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.Retrofit.studentRatingPlan.ControlDot
import com.example.sucrose.R

class AdapterRatingScoreChild(private val controldots: List<ControlDot>) :
    RecyclerView.Adapter<AdapterRatingScoreChild.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ratingscore_child, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = controldots[position]
        val Date = currentItem.Date
        val datetime: String = if (Date != null) {
            val parts =
                Date.substringBefore("T").split("-")
            "${parts[2]}.${parts[1]}.${parts[0]}"
        } else {
            "-"
        }

        if (currentItem.IsReport) {//имеется ли поле для прикрепления файл
            holder.file_icon.visibility = View.VISIBLE
            if (currentItem.Report != null)
                holder.file_icon.setColorFilter(
                    ContextCompat.getColor(
                        holder.file_icon.context,
                        R.color.green
                    )
                )
            else
                holder.file_icon.setColorFilter(
                    ContextCompat.getColor(
                        holder.file_icon.context,
                        R.color.gray
                    )
                )
        } else
            holder.file_icon.visibility = View.GONE

        val maxball = if (currentItem.MaxBall != null) currentItem.MaxBall else 0
        val ball = if (currentItem.Mark != null) currentItem.Mark.Ball else 0
        val title = if (currentItem.Title != null) currentItem.Title else "-"

        holder.time_ratingchild.text = datetime
        holder.text_controldot.text = title//заголовок
        holder.scorechild.text = ball.toString() + "/" + maxball.toString()
    }

    override fun getItemCount(): Int {
        return controldots.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_controldot: TextView = itemView.findViewById(R.id.text_controldot)
        val scorechild: TextView = itemView.findViewById(R.id.scorechild)
        val time_ratingchild: TextView = itemView.findViewById(R.id.time_ratingchild)
        val file_icon: ImageView = itemView.findViewById(R.id.file_icon)
    }
}