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
           val inputsLimit: Int = 1,
           val outputsLimit: Int = 1,
           val event: OnEvent? = null,
           val isStatic: Boolean = false,
           var color: Int = -1) {

    lateinit var view: SlotView
    lateinit var parent: NodeView

    var outputs: ArrayList<Slot>? = null
    var inputs: ArrayList<Slot>? = null

    fun createLink(out: Slot) {

        // if we have too much link, we remove the first one to be able to create a new one
        if(outputs != null && outputs!!.size >= outputsLimit) {
            val previousLinkedSlot = outputs!!.removeAt(0)
            previousLinkedSlot.inputs?.remove(this)
        }

        // if the out slot is full (already reached the maximum number of input we remove one
        if(out.inputs != null && out.inputs!!.size >= out.inputsLimit) {
            val previousLinkedSlot = out.inputs!!.removeAt(0)
            previousLinkedSlot.outputs!!.remove(out)
        }

        this.outputs = (this.outputs?:ArrayList()).plus(out) as ArrayList<Slot>
        out.inputs = (out.inputs?:ArrayList()).plus(this) as ArrayList<Slot>
    }

    enum class Positions {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    interface OnEvent {
        fun onSlotClicked(context: Context, slot: Slot, view: NodeView, flowChart: FlowChart)
        //fun onLink(self: Slot, dest: Slot): Boolean // TODO: NO IMPLEMENTED YET
    }
}