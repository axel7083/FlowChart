package com.github.axel7083.flowchart

import android.content.Context
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import com.github.axel7083.flowchart.views.NodeView

class IntSumNode : Node, Slot.OnEvent {

    override val slots = arrayListOf(
            Slot(id=0, str="+", position=Slot.Positions.TOP, isInput=true,event=this),
            Slot(id=1, str="=", position=Slot.Positions.BOTTOM, isInput=false),
    )

    override fun execute(args: Array<Any>?): Array<Any>? {
        if(args == null || args.isEmpty())
            return arrayOf(0L)

        var sum = 0L
        for(v in args) {
            sum+= v as Long
        }

        return arrayOf(sum)
    }

    var i: Long = 0
    override fun onClick(context: Context,slot: Slot, view: NodeView, flowChart: FlowChart) {
        i++
        view.addSlot(Slot(i,"\\/",Slot.Positions.TOP,true),flowChart)
        flowChart.invalidate()
    }

    override fun onLink(self: Slot, dest: Slot): Boolean {
        return true
    }
}
