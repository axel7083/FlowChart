package com.github.axel7083.flowchart

import android.graphics.Point
import android.os.Parcel

import android.os.Parcelable
import android.os.Parcelable.Creator
import android.view.View
import com.github.axel7083.flowchart.models.Node


class SavedState : View.BaseSavedState {

    var size : Int = 1
    lateinit var nodes: ArrayList<Node>
    lateinit var coords: ArrayList<Point>
    lateinit var links: ArrayList<FlowChart.Link>


    constructor(superState: Parcelable?) : super(superState) {}
    private constructor(`in`: Parcel) : super(`in`) {

        size = `in`.readInt()
        `in`.readList(nodes,nodes::class.java.classLoader)
        `in`.readList(coords,coords::class.java.classLoader)
        `in`.readList(links,links::class.java.classLoader)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(size)
        out.writeList(nodes)
        out.writeList(coords)
        out.writeList(links)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<SavedState> {
        override fun createFromParcel(parcel: Parcel): SavedState {
            return SavedState(parcel)
        }

        override fun newArray(size: Int): Array<SavedState?> {
            return arrayOfNulls(size)
        }
    }
}