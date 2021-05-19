package com.github.axel7083.flowchartexample.models

import android.content.Context
import android.util.Log
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import com.github.axel7083.flowchart.views.NodeView

class IntSumNode : Node(), Slot.OnEvent {

    override val title: String = "Sum Node"
    override val description: String = "Sum inputs and output value"

    override val slots = arrayListOf(
            Slot(id=0, str="+", position=Slot.Positions.TOP, isInput=false,event=this, isStatic=true),
            Slot(id=1, str="=", position=Slot.Positions.BOTTOM, isInput=false),
    )

    override fun execute(args: Array<*>?, context: Context, view: NodeView, flowChart: FlowChart): Array<Any>? {
        if(args == null || args.isEmpty()) {
            Log.d(TAG, "execute: Empty")
            return arrayOf(0)
        }
        Log.d(TAG, "execute: size: ${args.size}")
        var sum = 0
        for(v in args) {
            sum+= v as Int
        }

        Log.d(TAG, "execute: total: $sum")
        return arrayOf(sum)
    }

    var i: Long = 2
    override fun onSlotClicked(context: Context, slot: Slot, view: NodeView, flowChart: FlowChart) {
        slots.add(view.addSlot(Slot(i,"\\/",Slot.Positions.TOP,true),flowChart))
        flowChart.invalidate()
        i++
    }

    override fun onNodeClicked(context: Context, view: NodeView, flowChart: FlowChart) {

    }

    companion object {
        private const val TAG = "IntSumNode"
    }
}
