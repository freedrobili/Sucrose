package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.R

class MyAdapterControlPoint(private val controlpointList: ArrayList<ControlDotView>) :
    RecyclerView.Adapter<MyAdapterControlPoint.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_view_row_controlpoint, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = controlpointList[position]

        val cardview = holder.tvHeading
        cardview.text = currentItem.Title
        if (currentItem.Ball != 0.0) {
            holder.score_pointscore.text = currentItem.Ball.toString()
        }

        if (currentItem.Heading) {//Заголовок
            holder.box_item_controlpoint.setBackgroundColor(
                ContextCompat.getColor(
                    cardview.context,
                    R.color.light_purple
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return controlpointList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeading: TextView = itemView.findViewById(R.id.text_point)
        val box_item_controlpoint: CardView = itemView.findViewById(R.id.box_item_controlpoint)
        val score_pointscore: TextView = itemView.findViewById(R.id.score_pointscore)
    }
}