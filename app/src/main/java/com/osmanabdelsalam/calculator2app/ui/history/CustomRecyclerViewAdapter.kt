package com.osmanabdelsalam.calculator2app.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.osmanabdelsalam.calculator2app.R
import com.osmanabdelsalam.calculator2app.data.History

class CustomRecyclerViewAdapter(private val historyListItems: List<History>): RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
        val tvHistoryItem: TextView = viewItem.findViewById(R.id.tv_history_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_history_item,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return historyListItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyListItems[position]
        holder.tvHistoryItem.text = historyItem.text
    }
}