package com.github.axel7083.flowchartexample.models

import android.content.Context
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import com.github.axel7083.flowchart.views.NodeView

class DisplayInputNode : Node {

    override val title: String = "Display Node"
    override val description: String = "Execute to show what is inputted"

    override val slots = arrayListOf(
            Slot(id=0, str="IN", position=Slot.Positions.TOP, isInput=true),
    )

    override fun execute(args: Array<*>?, context: Context, view: NodeView, flowChart: FlowChart): Array<Any>? {
        view.setDescription("Inputted value ${args?.get(0)}")
        return null
    }

    override fun onNodeClicked(context: Context, view: NodeView, flowChart: FlowChart) {
        flowChart.deleteCard(view)
    }

}
