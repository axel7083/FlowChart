package com.github.axel7083.flowchart.models

import android.content.Context
import android.view.ViewGroup
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.views.NodeView
import java.io.Serializable
import java.lang.Exception
import kotlin.properties.Delegates

abstract class Node : Serializable {
    private var id: Long = -1L

    internal fun setId(id: Long): Boolean {
        if(this.id == -1L) {
            this.id = id
            return true
        }
        return false
    }

    fun getId(): Long {
        return id
    }

    abstract val slots: ArrayList<Slot>?
    abstract val title: String
    abstract val description: String
    abstract fun execute(args : Array<*>?, context: Context, view: NodeView, flowChart: FlowChart) : Array<Any>?
    open fun onNodeClicked(context: Context, view: NodeView, flowChart: FlowChart) {}
}