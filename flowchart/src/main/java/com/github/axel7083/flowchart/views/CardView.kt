package com.github.axel7083.flowchart.views

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.github.axel7083.flowchart.R
import com.github.axel7083.flowchart.databinding.ViewCardBinding

class CardView(context: Context) : LinearLayout(context) {
    val binding = ViewCardBinding.bind(inflate(context, R.layout.view_card,this))

    init {
        //this.radius = 0f
        this.gravity = Gravity.CENTER
        val d = ContextCompat.getDrawable(context, R.drawable.round_bg)
        d?.alpha = 215
        this.background = d
        this.elevation = 0f
    }
}