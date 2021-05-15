package com.github.axel7083.flowchartexample.models

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.LinearLayout
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import com.github.axel7083.flowchart.views.NodeView
import com.github.axel7083.flowchartexample.dialogs.SelectDialog

class IntegerOutputNode : Node {

    override val title: String = "Integer node"
    override val description: String = "(click to select)"

    private var value: Int = 0

    override val slots = arrayListOf(
            Slot(id=0, str="V", position=Slot.Positions.BOTTOM, isInput=false),
    )

    override fun execute(args: Array<*>?, context: Context, view: NodeView, flowChart: FlowChart): Array<Any>? {
        return arrayOf(value)
    }

    override fun onNodeClicked(context: Context, view: NodeView, flowChart: FlowChart) {
        val dialog = SelectDialog(context) { output ->
            if(output != null) {
                value = output
                view.setDescription("Current value $value")
            }
        }
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            window.setGravity(Gravity.CENTER)
            window.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
