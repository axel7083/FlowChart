package com.github.axel7083.flowchartexample.executers

import android.content.Context
import android.util.Log
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.views.NodeView

class Executer(val context: Context,val flowChart: FlowChart) {

    val values = HashMap<NodeView, Array<*>?>()

    fun start() {
        Log.d(TAG, "Start")
        for((_,nodeView) in flowChart.cardsHolder) {
            compute(nodeView)
            Log.d(TAG, "cardsHolder: Next")
        }
    }

    private fun compute(nodeView: NodeView, k: Int = 0) {
        if(values.containsKey(nodeView))
            return

        Log.d(TAG, getSpace(k)+"compute: title: ${nodeView.card.title.text} | ${nodeView.card.desc.text}")

        values[nodeView] = null

        val slots = nodeView.getSlots()
        val data: Array<Int> = Array(slots.first.size) {0}

        Log.d(TAG, getSpace(k)+"compute: has ${slots.first.size} inputs slot")
        // Iterating all inputs slot
        for(i in 0 until slots.first.size) {

            // Iterating all outputs (from other node link to the current input slot i )
            slots.first[i].inputs?.forEach { parentSlot ->
                Log.d(TAG, getSpace(k)+"compute: (parent title: ${parentSlot.parent.card.title.text})")
                // If the parent element has not been computed yet
                if(!values.containsKey(parentSlot.parent)) {
                    Log.d(TAG, getSpace(k)+"compute: computing parent start")
                    compute(parentSlot.parent, k + 1)
                    Log.d(TAG, getSpace(k)+"compute: computing parent finish ${nodeView.card.title.text}")
                }

                val index = parentSlot.parent.getOutputPos(parentSlot)
                val value = (values[parentSlot.parent] as Array<*>?)?.get(index) as Int?
                Log.d(TAG, getSpace(k)+"compute: parent computed, output index: $index | value: $value")
                data[i] += value?:0

            }
        }

        Log.d(TAG, getSpace(k)+"compute: computing current node")
        // We compute the current node
        values[nodeView] = nodeView.node.execute(data,context, nodeView, flowChart) as Array<*>?
        printArray(values[nodeView])

        Log.d(TAG, getSpace(k)+"compute: has ${slots.second.size} output slot")
        // Iterating all outputs slots
        for(i in 0 until slots.second.size) {

            // Iterating all inputs (from other node link to the current input slot i )
            slots.second[i].outputs?.forEach { childSlot ->

                Log.d(TAG, getSpace(k)+"compute: (child title: ${childSlot.parent.card.title.text})")

                // If the child element has not been computed yet
                if(!values.containsKey(childSlot.parent)) {
                    compute(childSlot.parent, k + 1) // Compute the child
                }
            }
        }
    }

    fun printArray(arr: Array<*>?) {
        if(arr != null ) {
            arr.forEach { v ->
                print("$v, ")
            }
            println()
        }
    }

    fun getSpace(k : Int) : String {
        var s = ""
        for(i in 0..k) {
            s+="  "
        }
        return s
    }

    companion object {
        private const val TAG = "Executer"
    }
}