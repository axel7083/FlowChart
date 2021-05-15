package com.github.axel7083.flowchartexample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.axel7083.flowchart.IntSumNode
import com.github.axel7083.flowchartexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addSum.setOnClickListener {
            binding.flow.addCard(IntSumNode())
        }

        binding.addDisplay.setOnClickListener {

        }
    }
}