package com.github.axel7083.flowchart.models

import android.content.Context
import android.graphics.Color
import android.view.View
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.views.NodeView
import com.github.axel7083.flowchart.views.SlotView

class Slot(val id: Long,
           val str: String,
           val position: Positions,
           val isInput: Boolean,
           val connectionLimit: Int = 1,
           val event: OnEvent? = null,
           var color: Int = -1,
           var links: List<Slot>? = null) {

    lateinit var view: SlotView
    lateinit var parent: NodeView

    enum class Positions {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    interface OnEvent {
        fun onClick(context: Context,slot: Slot, view: NodeView, flowChart: FlowChart)
        fun onLink(self: Slot, dest: Slot): Boolean
    }
}