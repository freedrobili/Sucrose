package com.example.sucrose.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.sucrose.R

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img = view.findViewById<ImageView>(R.id.znak_uni)
        img.alpha = 0f//not visible
        img.animate().setDuration(700).alpha(1f).withEndAction {
            findNavController().navigate(R.id.action_splashFragment2_to_loginFragment3) //переходим по маршруту навигации
        }
    }
}