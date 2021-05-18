package com.github.axel7083.flowchart.views

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.axel7083.flowchart.R

class CardView(context: Context) : LinearLayout(context) {
    val view: View = inflate(context, R.layout.view_card,this)
    var title: TextView = view.findViewById(R.id.title)
    var desc: TextView = view.findViewById(R.id.desc)

    init {

        //this.radius = 0f
        this.gravity = Gravity.CENTER
        val d = ContextCompat.getDrawable(context, R.drawable.round_bg)
        d?.alpha = 215
        this.background = d
        this.elevation = 0f
    }
}