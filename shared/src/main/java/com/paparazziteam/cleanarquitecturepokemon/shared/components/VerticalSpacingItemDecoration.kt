package com.paparazziteam.cleanarquitecturepokemon.shared.components

class VerticalSpacingItemDecoration(val spacing: Int, val includeEdge: Boolean) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        if (includeEdge) {
            outRect.top = spacing
            outRect.bottom = spacing
            if (position == 0) {
                outRect.top = 0
            }
            if (position == parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = 0
            }
        } else {
            if (position > 0) {
                outRect.top = spacing
            }
        }
    }
}