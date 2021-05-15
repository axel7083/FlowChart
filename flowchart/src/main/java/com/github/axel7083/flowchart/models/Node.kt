package com.github.axel7083.flowchart.models

import android.content.Context
import android.view.ViewGroup
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.views.NodeView

interface Node {
    val slots: ArrayList<Slot>?
    val title: String
    val description: String
    fun execute(args : Array<*>?, context: Context, view: NodeView, flowChart: FlowChart) : Array<Any>?
    fun onNodeClicked(context: Context, view: NodeView, flowChart: FlowChart) {}
}