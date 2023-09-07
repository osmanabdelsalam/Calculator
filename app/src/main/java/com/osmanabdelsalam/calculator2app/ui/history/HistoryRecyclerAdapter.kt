package com.osmanabdelsalam.calculator2app.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osmanabdelsalam.calculator2app.data.History
import com.osmanabdelsalam.calculator2app.databinding.CustomHistoryItemBinding

class HistoryRecyclerAdapter: ListAdapter<History, HistoryRecyclerAdapter.HistoryViewHolder>(HistoryDiffCallBack()) {

    private class HistoryDiffCallBack: DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return (oldItem.id == newItem.id && oldItem.text == newItem.text)
        }
    }

    class HistoryViewHolder(val binding: CustomHistoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            CustomHistoryItemBinding.inflate(
                parent.context.getSystemService(LayoutInflater::class.java),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int
    ) {
        holder.binding.tvHistoryItem.text = getItem(position).text
    }
}