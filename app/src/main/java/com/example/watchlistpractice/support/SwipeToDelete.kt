package com.example.watchlistpractice.support

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDelete(val myAdapter: ListCardAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        myAdapter.deleteItem(pos)
    }
}