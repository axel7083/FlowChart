package com.github.axel7083.flowchart.views

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.github.axel7083.flowchart.FlowChart
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

class NodeView(val context: Context, parent: ViewGroup) {

    lateinit var node: Node
    private var count: IntArray = IntArray(4)
    var slots: List<Slot> = ArrayList()

    var card: CardView = CardView(context)

    var minWidth = 0f
    var minHeight = 0f

    init {
        parent.addView(card)
    }

    fun getOutputPos(unknown: Slot): Int {
        var i = 0
        slots.forEach { slot ->
            if(!slot.isStatic) {
                if (!slot.isInput) {
                    if (unknown == slot)
                        return i
                    else
                        i++
                }
            }}
        return -1
    }

    fun getSlots(): Pair<ArrayList<Slot>, ArrayList<Slot>> {
        val inputs = ArrayList<Slot>()
        val outputs = ArrayList<Slot>()
        slots.forEach { slot ->
            if(!slot.isStatic) {
                if(slot.isInput)
                    inputs.add(slot)
                else
                    outputs.add(slot)
            }
        }
        return Pair(inputs, outputs)
    }

    fun getSlot(view: View): Slot? {
        slots.forEach { slot ->
            if(slot.view == view)
                return slot
        }
        return null
    }

    fun setTitle(str: String) {
        card.binding.title.text = str
    }

    fun setDescription(str: String) {
        card.binding.desc.text = str
    }

    fun computeMinimums() {
        // Checking the min dimension of the card
        minWidth = max((FlowChart.SPACING * 2f) * max(count[getIndex(Slot.Positions.BOTTOM)], count[getIndex(Slot.Positions.TOP)]), card.measuredWidth.toFloat())
        minHeight =  max((FlowChart.SPACING * 2f)  * max(count[getIndex(Slot.Positions.LEFT)], count[getIndex(Slot.Positions.RIGHT)]), card.measuredHeight.toFloat())
    }

    fun updatePos() {

        val indices: IntArray = IntArray(4)

        for (slot in slots) {

            var x = 0f
            var y = 0f

            // Computing X position
            if (slot.position == Slot.Positions.TOP || slot.position == Slot.Positions.BOTTOM) {
                x = card.x + (FlowChart.SPACING * 2f) * indices[getIndex(slot.position)] - slot.view.width / 2f + FlowChart.SPACING
            }

            if (slot.position == Slot.Positions.TOP) {
                y = card.y - slot.view.height / 2

                // Adjusting to be center
                val diffX = count[getIndex(Slot.Positions.BOTTOM)] - count[getIndex(Slot.Positions.TOP)]
                x += if (diffX > 0) diffX * (FlowChart.SPACING).toFloat() else 0f
            }

            if (slot.position == Slot.Positions.BOTTOM) {
                y = card.y + card.height - slot.view.height / 2

                // Adjusting to be center
                val diffX = count[getIndex(Slot.Positions.TOP)] - count[getIndex(Slot.Positions.BOTTOM)]
                x += if (diffX > 0) diffX * (FlowChart.SPACING).toFloat() else 0f
            }

            // Computing Y position
            if (slot.position == Slot.Positions.LEFT || slot.position == Slot.Positions.RIGHT) {
                y = card.y + (FlowChart.SPACING * 2f) * indices[getIndex(slot.position)] - slot.view.height / 2f + FlowChart.SPACING
            }

            if (slot.position == Slot.Positions.LEFT) {
                x = card.x - slot.view.width / 2f

                val diffY = count[getIndex(Slot.Positions.RIGHT)] - count[getIndex(Slot.Positions.LEFT)]
                y += if (diffY > 0) diffY * (FlowChart.SPACING).toFloat() else 0f
            }
            if (slot.position == Slot.Positions.RIGHT) {
                x = card.x + card.width - slot.view.width / 2f

                val diffY = count[getIndex(Slot.Positions.LEFT)] - count[getIndex(Slot.Positions.RIGHT)]
                y += if (diffY > 0) diffY * (FlowChart.SPACING).toFloat() else 0f
            }

            slot.view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(0)
                    .start()

            indices[getIndex(slot.position)]++
        }
    }


    fun addSlot(slot: Slot, viewGroup: ViewGroup): Slot {

        // Defining color if not already done
        if(slot.color == -1) {
            val rnd = Random()
            slot.color = Color.argb(210, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

        slot.view = inflateSlot(viewGroup, slot.color, slot.str)
        slot.view.slot = slot // Setting double linked reference

        slot.parent = this // Defining parent (could be useful)
        slots = slots.plus(slot)

        // We store the total number of slot per position
        count[getIndex(slot.position)]++


        return slot
    }

    private fun getIndex(i: Slot.Positions) = when(i) {
        Slot.Positions.BOTTOM -> 0
        Slot.Positions.LEFT -> 1
        Slot.Positions.TOP -> 2
        Slot.Positions.RIGHT -> 3 }

    private fun inflateSlot(parent: ViewGroup, color: Int, str: String): SlotView {
        val view = SlotView(context)
        view.binding.layout.setBackgroundColor(color)
        view.binding.value.text = str
        parent.addView(view)
        view.z = 99f
        return view
    }

    companion object {
        const val SLOT_SPACING = 50f
        fun getPixelSize(dp: Float, context: Context): Float {
            return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.resources.displayMetrics
            )
        }
    }


 }