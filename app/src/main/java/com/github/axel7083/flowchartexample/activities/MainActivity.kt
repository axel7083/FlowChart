package com.github.axel7083.flowchartexample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.axel7083.flowchart.Executer
import com.github.axel7083.flowchartexample.models.IntSumNode
import com.github.axel7083.flowchartexample.databinding.ActivityMainBinding
import com.github.axel7083.flowchartexample.models.DisplayInputNode
import com.github.axel7083.flowchartexample.models.IntegerOutputNode

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
            binding.flow.addCard(DisplayInputNode())
        }

        binding.addIntegerOutput.setOnClickListener {
            binding.flow.addCard(IntegerOutputNode())
        }

        binding.execute.setOnClickListener {
            val executer = Executer(this,binding.flow)
            executer.start()
        }
    }
}