package com.example.sucrose

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class GradeActivity : AppCompatActivity() {
    lateinit var navController: NavController

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade)

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.grade_main_fragment_conteiner) as NavHostFragment)
        navController = navHostFragment.navController
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.grade_bottomNavigationView)
        setupWithNavController(bottomNavigationView, navController)

        val text = findViewById<TextView>(R.id.name_discipline)
        text.text = intent.getStringExtra("name_discipline")
        val back = findViewById<AppCompatImageView>(R.id.button_back_main)
        back.setOnClickListener {
            finish()
        }
    }
}