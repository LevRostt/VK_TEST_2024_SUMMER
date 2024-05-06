package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = (parent.layoutManager as? LinearLayoutManager)?.itemCount ?: 0

        if (position == 0) {
            outRect.left = spacing / 2
        }
        if (position != itemCount)
            outRect.right = spacing
    }
}