package com.github.axel7083.flowchartexample.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.github.axel7083.flowchartexample.databinding.DialogInputBinding


class SelectDialog(context: Context,
                   private val callback: (Int?) -> Unit) : Dialog(context) {

    private lateinit var binding: DialogInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            callback(null)
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            callback(Integer.parseInt(binding.inputValue.text.toString()))
            dismiss()
        }
    }

}