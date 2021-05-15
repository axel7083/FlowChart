package com.github.axel7083.flowchart.models

import android.view.ViewGroup

interface Node {
    val slots: ArrayList<Slot>?
    fun execute(args : Array<Any>?) : Array<Any>?
}