package com.example.sucrose.recycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.retrofit.forumMessage.ForumMessage
import com.example.sucrose.R

// Адаптер для RecyclerView, отображающий сообщения в чате
class AdapterChat(private val chatlist: ForumMessage, private val buttonClickListener: ButtonClickListener): RecyclerView.Adapter<AdapterChat.AdapterChatViewHolder>() {

    // Создание нового ViewHolder при необходимости
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  AdapterChatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_chat_item, parent, false)
        return  AdapterChatViewHolder(itemView, buttonClickListener)
    }

    // Привязка данных к ViewHolder при прокрутке RecyclerView
    override fun onBindViewHolder(holder: AdapterChatViewHolder, position: Int) {
        val currentItem = chatlist[position]
        holder.time_part.visibility = View.GONE // Скрытие части с датой

        // Извлечение данных из текущего элемента
        val fio = currentItem.User.FIO
        val Createdate = currentItem.CreateDate
        val text = currentItem.Text

        // Форматирование даты и времени
        val parts = Createdate.substringBefore("T").split("-")
        var datetime = "${parts[2]}.${parts[1]}.${parts[0]} ${Createdate.substringAfter('T').take(5)}"

        // Отображение сообщений в зависимости от отправителя
        if (currentItem.User.FIO == "Кручинкин Александр Александрович") {
            holder.card_otherusermessage.visibility = View.GONE
            holder.card_mymessage.visibility = View.VISIBLE
            holder.name_sms_user.text = fio
            holder.textmessage_sms_user.text = currentItem.Text
            holder.timedate_sms_user.text = datetime
        } else {
            holder.card_otherusermessage.visibility = View.VISIBLE
            holder.card_mymessage.visibility = View.GONE
            holder.name_sms_otheruser.text = fio
            holder.textmessage_sms_otheruser.text = currentItem.Text
            holder.timedate_sms_otheruser.text = datetime
        }
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int {
        return chatlist.size
    }

    // ViewHolder для отдельного элемента списка
    inner class AdapterChatViewHolder(itemView: View, listener: ButtonClickListener): RecyclerView.ViewHolder(itemView) {
        // Элементы интерфейса
        val delete_message: ImageView = itemView.findViewById(R.id.delete_sms)
        val time_part: ConstraintLayout = itemView.findViewById(R.id.time_part)
        val message_part: ConstraintLayout = itemView.findViewById(R.id.message_part)

        val card_otherusermessage: CardView = itemView.findViewById(R.id.other_usermessage)
        val textmessage_sms_otheruser: TextView = itemView.findViewById(R.id.textmessage_sms_otheruser)
        val name_sms_otheruser: TextView = itemView.findViewById(R.id.name_sms_otheruser)
        val timedate_sms_otheruser: TextView = itemView.findViewById(R.id.timedate_sms_otheruser)

        val card_mymessage: CardView = itemView.findViewById(R.id.card_mymessage)
        val textmessage_sms_user: TextView = itemView.findViewById(R.id.textmessage_sms_user)
        val name_sms_user: TextView = itemView.findViewById(R.id.name_sms_user)
        val timedate_sms_user: TextView = itemView.findViewById(R.id.timedate_sms_user)

        // Инициализация слушателя событий для кнопки в ViewHolder
        init {
            delete_message.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onButtonClicked(position)
                }
            }
        }
    }

    // Интерфейс для обработки событий кнопок в элементах списка
    interface ButtonClickListener {
        fun onButtonClicked(position: Int)
    }
}
