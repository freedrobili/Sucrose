package com.example.sucrose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class MyAdapter(private val disciplineList: ArrayList<Discipline_item>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_row, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = disciplineList[position]
        val cardview = holder.card_box_item
        val params = cardview.layoutParams as ViewGroup.MarginLayoutParams
        val resourse_card = cardview.context.resources

        holder.tvHeading.text = currentItem.text

        if (currentItem.heading) {
            cardview.setCardBackgroundColor(
                ContextCompat.getColor(
                    cardview.context,
                    R.color.light_purple
                )
            )
            params.height = resourse_card.getDimensionPixelSize(R.dimen.margin_65dp)
            holder.tvHeading.setTextColor(ContextCompat.getColor(cardview.context, R.color.white))

            params.marginStart = resourse_card.getDimensionPixelSize(R.dimen.margin_minus_10dp)
            cardview.layoutParams = params
        } else {
            cardview.setCardBackgroundColor(ContextCompat.getColor(cardview.context, R.color.white))
            holder.tvHeading.setTextColor(ContextCompat.getColor(cardview.context, R.color.black))

            params.marginStart = resourse_card.getDimensionPixelSize(R.dimen.margin_20dp)
            cardview.layoutParams = params
        }
    }

    override fun getItemCount(): Int {
        return disciplineList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val tvHeading: TextView = itemView.findViewById(R.id.recycle_text)
        val card_box_item: CardView = itemView.findViewById(R.id.Card_box_item)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }
}