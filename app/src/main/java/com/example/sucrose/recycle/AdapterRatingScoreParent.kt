package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.retrofit.studentRatingPlan.Section
import com.example.sucrose.R

class AdapterRatingScoreParent(private val sections: List<Section>) :
    RecyclerView.Adapter<AdapterRatingScoreParent.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_raitingscore_parent, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: Section = sections[position]//текущий элемент
        val ControlDots = currentItem.ControlDots//контрольные точки во вложенный RecyclerView


        val heading =
            if (currentItem.Title != null) currentItem.Title else "-"
        holder.Title_ratingscore.text = heading//Заголовок контролных точек

        //вложенный recycler
        holder.recycler_ratingscore_child.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        holder.recycler_ratingscore_child.setHasFixedSize(true)
        holder.recycler_ratingscore_child.adapter = AdapterRatingScoreChild(ControlDots)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Title_ratingscore: TextView = itemView.findViewById(R.id.Title_ratingscore)
        val recycler_ratingscore_child: RecyclerView =
            itemView.findViewById(R.id.recycler_ratingscore_child)
    }
}