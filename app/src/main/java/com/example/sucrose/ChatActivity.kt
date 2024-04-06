package com.example.sucrose

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sucrose.recycle.AdapterChat
import com.example.sucrose.Retrofit.ForumMessage.ForumMessage
import com.example.sucrose.Retrofit.ForumMessage.ForumMessageItem
import com.example.sucrose.Retrofit.message.Message
import com.example.sucrose.Retrofit.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatActivity : AppCompatActivity(), AdapterChat.ButtonClickListener {
    @SuppressLint("MissingInflatedId")
    lateinit var newRecycle: RecyclerView
    var context = this
    lateinit var adapter: AdapterChat
    private val retrofit = buildExRetrofit("https://papi.mrsu.ru/")
    private val userService = retrofit.create(UserService::class.java)
    lateinit var listmassage: ForumMessage
    private lateinit var Auth: String
    private lateinit var id_discipline: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val token = getToken("private")
        val authHeader = "Bearer $token"
        Auth = authHeader
        id_discipline = intent.getStringExtra("id_discipline_forChat").toString()//id дисциплины

        val name_discipline = intent.getStringExtra("name_discipline_forChat")// имя дисциплины
        val sendmessage_button: ImageView = findViewById(R.id.sendmessage_button)
        println(id_discipline + " " + name_discipline)
        val text_chatname = findViewById<TextView>(R.id.text_chatname)
        text_chatname.text = "($name_discipline)"

        newRecycle = findViewById(R.id.recycler_chat)
        newRecycle.layoutManager = LinearLayoutManager(this)
        newRecycle.setHasFixedSize(true)
        getForumMessage(authHeader, id_discipline!!.toInt(), 250)

        val text_massagechat: EditText = findViewById(R.id.text_massagechat)
        var message_t = text_massagechat.text
        sendmessage_button.setOnClickListener {
            println("Попытка отправить sms")
            val message = Message(message_t.toString())
            postForumMessage(authHeader, id_discipline!!.toInt(), message)

            //  getForumMessage(authHeader,id_discipline!!.toInt(),250)
        }
    }

    private fun deleteForumMessage(token: String, messageId: Int, position: Int) {
        val call = userService.deleteMessage(token, messageId.toString())
        println("Попытка удалить sms Function")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val data = response.body()//data
                    listmassage.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    println("Успешно удалили смс")
                } else {
                    println("ChatActivity " + " Что-то пошло не так  onResponse(delete)")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("ChatActivity " + " Что-то пошло не так  onFailure(delete)")
            }
        })

    }

    private fun postForumMessage(token: String, disciplineId: Int, message: Message) {
        println("Попытка отправить sms Function")
        val call = userService.sendMessage(token, disciplineId.toString(), message)
        println("Попытка отправить sms Function")
        call.enqueue(object : Callback<ForumMessageItem> {
            override fun onResponse(
                call: Call<ForumMessageItem>,
                response: Response<ForumMessageItem>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()//data
                    if (data != null) {
                        listmassage.add(data)
                        adapter.notifyDataSetChanged()
                        println("Успешно отправили смс")
                    }

                } else {
                    println("ChatActivity " + " Что-то пошло не так  onResponse(Post)")
                }
            }

            override fun onFailure(call: Call<ForumMessageItem>, t: Throwable) {
                println("ChatActivity " + " Что-то пошло не так  onFailure(post)")
            }
        })

    }

    private fun getForumMessage(token: String, disciplineId: Int, count: Int = 100) {
        if (disciplineId != null) {
            println(disciplineId)
            val call = userService.getForumMessage(
                token = token,
                disciplineId = disciplineId,
                count = count
            )
            call.enqueue(object : Callback<ForumMessage> {
                override fun onResponse(
                    call: Call<ForumMessage>,
                    response: Response<ForumMessage>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()//data
                        if (data != null) {//not null
                            data.reverse()

                            listmassage = data
                            adapter = AdapterChat(data, this@ChatActivity)
                            //adapter.notifyDataSetChanged()//сообщаем адаптеру что набор данных изменился
                            newRecycle.adapter = adapter

                            println("Data of MessageForum $data")
                        } else {
                            println("ChatActivity " + " Сообщений нет")
                        }
                    } else {
                        println("ChatActivity " + " Что-то пошло не так  onResponse(Get)")
                    }
                }

                override fun onFailure(call: Call<ForumMessage>, t: Throwable) {
                    println("ChatActivity " + " Что-то пошло не так  onFailure")
                }
            })
        }
    }

    private fun getToken(key: String): String {
        val sharedPref = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val token_l = sharedPref.getString(key, null).toString()
        return token_l
    }

    private fun saveToken(token: String, key: String) {
        val sharedPref = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
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

    override fun onButtonClicked(position: Int) {

        println("Position clikeed button " + position + "")
        var size: Int = listmassage.size
        if (size != 0 && size > position) {//не выйти за границы списка
            println(listmassage[position].Text)
            var messageid = listmassage[position].Id
            println(messageid)
            deleteForumMessage(Auth, messageid, position)//удалить смс
            //getForumMessage(Auth,id_discipline!!.toInt(),250)//обновить сообщения
        }
    }
}
