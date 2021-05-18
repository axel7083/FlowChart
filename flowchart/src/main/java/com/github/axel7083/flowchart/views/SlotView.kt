package com.github.axel7083.flowchart.views

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.github.axel7083.flowchart.R
import com.github.axel7083.flowchart.models.Slot

class SlotView(context: Context) : CardView(context) {
    val view = inflate(context, R.layout.view_slot,this)
    val value = view.findViewById<TextView>(R.id.value)
    var linearLayout: LinearLayout = view.findViewById(R.id.layout)

    lateinit var slot: Slot // reference to parent object

    init {
        this.radius = 0f
        this.setBackgroundColor(Color.TRANSPARENT)
        this.elevation = 0f
    }
}