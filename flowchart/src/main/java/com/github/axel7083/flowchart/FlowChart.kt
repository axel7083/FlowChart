package com.github.axel7083.flowchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.axel7083.flowchart.models.Node
import com.github.axel7083.flowchart.models.Slot
import com.github.axel7083.flowchart.views.NodeView
import com.github.axel7083.flowchart.views.SlotView
import kotlin.math.max


class FlowChart : ViewGroup {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    val cardsHolder : HashMap<View, NodeView> = HashMap()

    private val paintBg = Paint()
    private var bufferLine: Pair<Float, Float>? = null

    private val slotsSize = NodeView.getPixelSize(50f, context).toInt()

    private var size: Int = 1

    init {
        isSaveEnabled = true
        setSize()

        paintBg.color = Color.GRAY
        paintBg.isAntiAlias = true



        //testing purpose
        /*val n = addCard()
        val o = n.addOutput(this, "?", Color.RED)
        slotsHolder[o] = n
        val n2 = addCard()
        n.link(o, n2)
        n2.card.y = 500f*/
    }

    private fun setSize() {
        val s = NodeView.getPixelSize(DEFAULT_SIZE * size, context).toInt()
        this.layoutParams = FrameLayout.LayoutParams(s,s)
    }

    fun addCard(node: Node, x : Float = 200f, y: Float = 200f) {
        val nodeView = NodeView(context, this)
        nodeView.node = node
        nodeView.setTitle(node.title)
        nodeView.setDescription(node.description)

        nodeView.card.x = x
        nodeView.card.y = y

        node.slots?.forEach { slot ->
            nodeView.addSlot(slot, this)
            if(slot.event != null) {
                slot.view.setOnClickListener {view ->
                    slot.event.onSlotClicked(context,slot, nodeView, this)
                }
            }
            else if(!slot.isInput) {
                registerDragger(slot)
            }
        }

        nodeView.card.setOnTouchListener(draggableListener)

        nodeView.card.setOnClickListener {
            node.onNodeClicked(context,nodeView, this)
        }

        cardsHolder[nodeView.card] = nodeView
        invalidate()
    }

    fun deleteCard(nodeView: NodeView) {
        cardsHolder.remove(nodeView.card)
        removeView(nodeView.card)

        nodeView.node.slots?.forEach { slot ->
            removeView(slot.view)
            slot.removeAllLinks()
        }
    }

    fun registerDragger(slot: Slot) {
        Log.d(TAG, "addCard: set outputListener")
        slot.view.isClickable = false
        slot.view.setOnTouchListener(outputListener)
    }

    private val outputListener = object : OnTouchListener {
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x + view.width / 2
                    dY = view.y + view.height / 2
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d(TAG, "onTouch: outputListener MOVE")
                    var x = event.x + dX
                    var y = event.y + dY
                    bufferLine = Pair(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    bufferLine = null
                    var x = event.x + dX
                    var y = event.y + dY

                    // Connect the current line with a slot (if possible)
                    for ((_, nodeView) in cardsHolder) {
                        if (nodeView == (view as SlotView).slot.parent) {
                            Log.d(TAG, "onTouch: self checking: useless")
                            continue
                        }
                        Log.d(TAG, "UP on (${x}:${y})")

                        for (slotTo in nodeView.slots) {
                            if (x >= slotTo.view.x
                                    && x <= slotTo.view.x + slotTo.view.width
                                    && y >= slotTo.view.y
                                    && y <= slotTo.view.y + slotTo.view.height
                            ) {

                                view.slot.createLink(slotTo)
                                Log.d(TAG, "onTouch: creating link")
                                break;
                            }
                        }

                    }
                    invalidate()

                }
                else -> return false
            }
            return true
        }
    }

    private var lastTouch: Long = 0
    private val draggableListener = object : OnTouchListener {
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouch = System.currentTimeMillis()
                    Log.d(TAG, "onTouch: DOWN")
                    dX = event.x
                    dY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    if(System.currentTimeMillis()-lastTouch < 250) {
                        view.performClick()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if(System.currentTimeMillis()-lastTouch < 100) return false

                    Log.d(TAG, "onTouch: MOVE")
                    var x = event.x + view.x - dX
                    var y = event.y + view.y - dY
                    x += (SPACING - (x % SPACING))
                    y += (SPACING - (y % SPACING))
                    view.x = x
                    view.y = y
                    cardsHolder[view]?.updatePos()
                    invalidate()
                }
                else -> {
                    Log.d(TAG, "onTouch: ELSE")
                    return false
                }
            }

            return true
        }
    }

    override fun dispatchDraw(canvas: Canvas) {

        var maxX = 0f
        var maxY = 0f

        for((view,_) in cardsHolder) {
            // Checking pos
            if(view.x > maxX)
                maxX = view.x
            if(view.y > maxY)
                maxY = view.y
        }

        var s = NodeView.getPixelSize(DEFAULT_SIZE * size, context).toInt()
        s -= NodeView.getPixelSize(DEFAULT_SIZE / 3, context).toInt()
        //Log.d(TAG, "onLayout: maxX $maxY maxY $maxY")
        if(maxX > s || maxY > s) {
            Log.d(TAG, "onLayout: increasing size")
            size++
            setSize()
        }

        //Draw background
        for(y in 0..height step SPACING) {
            for(x in 0..width step SPACING) {
                canvas.drawCircle(x.toFloat(), y.toFloat(), 5f, paintBg)
            }
        }

        // Draw lines
        val p = Paint()
        p.color = Color.GRAY
        p.strokeWidth = 10f
        if(bufferLine != null) {
            canvas.drawLine(dX, dY, bufferLine!!.first, bufferLine!!.second, p)
        }

        for((_, nodeView) in cardsHolder) {

            nodeView.slots.forEach { slotFrom ->

                slotFrom.outputs?.forEach { slotTo ->
                    if(slotTo.isInput) {
                        var fromX = slotFrom.view.x + slotFrom.view.width/2
                        var fromY = slotFrom.view.y + slotFrom.view.height/2
                        val toX = slotTo.view.x + slotTo.view.width/2
                        val toY = slotTo.view.y + slotTo.view.height/2

                        p.color = slotTo.color
                        (slotFrom.view).linearLayout.setBackgroundColor(p.color)

                        drawPath(canvas, fromX, fromY, toX, toY, nodeView, slotTo.parent, p)
                    }
                }
            }
        }

        //Draw views
        super.dispatchDraw(canvas)

    }

    // TODO: make output transparent when draging line from output
    private fun drawPath(canvas: Canvas, fromX: Float, fromY: Float, toX: Float, toY: Float, from: NodeView, to: NodeView, p: Paint) {
        val space = NodeView.getPixelSize(NodeView.SLOT_SPACING, context)
        val margin = NodeView.getPixelSize(10f, context)
        val pos = 0 //TODO: temp

        var newFromX = 0f
        var newFromY = 0f

        when(pos) {
            BOTTOM -> {
                canvas.drawLine(fromX, fromY,
                        fromX, fromY + space, p)
                newFromX = fromX
                newFromY = fromY + space

                if (fromY < (toY - 2*space)) {
                    canvas.drawLine(newFromX, newFromY,
                            newFromX, toY - space, p)
                    newFromY = toY - space
                    canvas.drawLine(newFromX, newFromY,
                            toX, newFromY, p)
                    newFromX = toX
                    canvas.drawLine(newFromX, newFromY,
                            toX, toY, p)
                    return
                } else {
                    val tempX = if (fromX < toX) toX - (to.card.width / 2 + margin) else toX + (to.card.width / 2 + margin)

                    canvas.drawLine(newFromX, newFromY,
                            tempX, newFromY, p)
                    newFromX = tempX

                    canvas.drawLine(newFromX, newFromY,
                            newFromX, toY - space, p)
                    newFromY = toY - space

                    canvas.drawLine(newFromX, newFromY,
                            toX, newFromY, p)
                    newFromX = toX

                    canvas.drawLine(newFromX, newFromY,
                            toX, toY, p)
                    return
                }


            }
            LEFT -> {
                canvas.drawLine(fromX, fromY,
                        fromX - space, fromY, p)
                newFromX = fromX - space
                newFromY = fromY

                // We go on the left
                if (newFromX > toX) {

                } else // We go on the right (must avoid from card)
                {

                }
            }
            RIGHT -> {
                canvas.drawLine(fromX, fromY,
                        fromX + space, fromY, p)
                newFromX = fromX + space
                newFromY = fromY
            }
        }

        // Unfinished section
        p.color = Color.RED
        canvas.drawLine(newFromX, newFromY,
                toX, toY, p)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout")

        //get the available size of child view
        val childLeft = 0
        val childTop = 0
        val childRight = this.measuredWidth
        val childBottom = this.measuredHeight
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop

        val defaultWidth = NodeView.getPixelSize(MIN_WIDTH, context)
        val defaultHeight = NodeView.getPixelSize(MIN_HEIGHT, context)

        for((card, node) in cardsHolder) {

            // Measure and layout the slots
            node.slots.forEach { slot ->
                slot.view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST))
                slot.view.layout(0, 0, slotsSize, slotsSize)
            }

            // Compute card minimum dimension (depending on slots size)
            node.computeMinimums()

            // Measure main card and layout it
            card.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST))
            card.layout(0, 0, max(node.minWidth,defaultWidth).toInt(), max(node.minHeight,defaultHeight).toInt())

            // Set nodes positions
            node.updatePos()
        }
    }

    inner class Data {
        var size = 1
        var nodes = ArrayList<Node>()
        var points = ArrayList<Point>()
    }

    fun getData(): Data {

        val data = Data()
        val nodeViews = cardsHolder.values
        nodeViews.forEach { nodeView ->
            data.nodes.add(nodeView.node)
            data.points.add(Point(nodeView.card.x.toInt(),nodeView.card.y.toInt()))
        }
        data.size = size
        return data
    }

    fun restoreData(data: Data) {
        this.size = data.size
        setSize()

        for(i in 0 until data.nodes.size) {
            addCard(
                data.nodes[i],
                data.points[i].x.toFloat(),
                data.points[i].y.toFloat()
            )
        }
        requestLayout()
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable {
        // Obtain any state that our super class wants to save.
        val superState = super.onSaveInstanceState()
        val data = getData()
        val savedState = SavedState(superState)

        savedState.nodes = data.nodes
        savedState.size = data.size
        savedState.coords = data.points

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        val data = Data()
        data.nodes  = savedState.nodes
        data.points = savedState.coords
        data.size = savedState.size

        restoreData(data)
    }

    companion object {
        private const val TAG = "FlowGraph"
        const val SPACING = 100
        const val MIN_WIDTH = 150f
        const val MIN_HEIGHT = 100f
        const val DEFAULT_SIZE = 1000f
        const val BOTTOM = 0
        const val LEFT = 1
        const val RIGHT = 2
    }

    var dX: Float = 0.0f
    var dY: Float = 0.0f
}