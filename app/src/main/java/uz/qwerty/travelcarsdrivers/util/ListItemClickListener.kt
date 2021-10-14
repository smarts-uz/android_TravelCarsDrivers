package uz.qwerty.travelcarsdrivers.util

import android.view.View

interface ListItemClickListener {
    fun onRetryClick(view: View, position: Int)
    fun onItemClick(id: Int)
}