package com.fakepepsilol.grasp.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class ObservableList<T>(
    private val _list: SnapshotStateList<T> = mutableStateListOf(),
    private val onChange: () -> Unit
) : MutableList<T> by _list {

    val TAG = "fpl->ObservableList"
    override fun add(element: T): Boolean {
        Log.d(TAG, "added element: $element")
        val result = _list.add(element)
        if (result) onChange()
        return result
    }

    override fun remove(element: T): Boolean {
        val result = _list.remove(element)
        if (result) onChange()
        return result
    }

}