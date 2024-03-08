package com.payxpert.game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.payxpert.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "Hangman ${MainActivity::class.java.simpleName}"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "##############################  START  ##############################")
    }
}