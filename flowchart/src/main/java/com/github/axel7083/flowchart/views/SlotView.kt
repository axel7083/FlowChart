package com.github.axel7083.flowchart.views

import android.content.Context
import android.graphics.Color
import androidx.cardview.widget.CardView
import com.github.axel7083.flowchart.R
import com.github.axel7083.flowchart.databinding.ViewSlotBinding
import com.github.axel7083.flowchart.models.Slot

class SlotView(context: Context) : CardView(context) {
    val binding = ViewSlotBinding.bind(inflate(context, R.layout.view_slot,this))

    lateinit var slot: Slot // reference to parent object

    init {
        this.radius = 0f
        this.setBackgroundColor(Color.TRANSPARENT)
        this.elevation = 0f
    }
}