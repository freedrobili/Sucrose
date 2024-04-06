package com.example.sucrose.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.hu_tao.Retrofit.TokenResponse
import com.example.sucrose.R
import com.example.sucrose.Retrofit.AuthService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private val retrofit_token = buildExRetrofit("https://p.mrsu.ru/");
    private val authService = retrofit_token.create(AuthService::class.java);
    private var successRequsets: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.sign_in)
        val login_f: EditText = view.findViewById(R.id.user_name)
        val password_f: EditText = view.findViewById(R.id.Password)
        val error: TextView = view.findViewById(R.id.message_error)
//        val login=(login_f.text)
//        val password=password_f.text

        val login = "kudashkina-yuliya@mail.ru"
        val password = "jnrelfyflj159"

        button.setOnClickListener {
            getStudentToken(login, password, error)
        }
    }

    private fun getStudentToken(login: String, password: String, error: TextView) {
        val call_token =
            authService.getToken("password", login, password, "8", "qweasd")
        call_token.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.access_token
                    if (token != null) {
                        saveToken(token, "private")
                        println("token =========== $token")
                        findNavController().navigate(R.id.action_loginFragment3_to_mainFragment2)//переход на главный экран
                    }
                } else {
                    error.text = "Неверное имя пользовaтеля или пaроль"
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                error.text = "Что то пошло не так"
            }
        })
    }

    private fun saveToken(token: String, key: String) {
        val sharedPref = requireActivity().getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, token)
        editor.apply()
    }

    private fun saveStringArrayToSharedPreferences(
        context: Context,
        nameShared: String,
        key: String,
        value: List<String>
    ) {
        val sharedPreferences = context.getSharedPreferences(nameShared, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(value)
        editor.putString(key, json)
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