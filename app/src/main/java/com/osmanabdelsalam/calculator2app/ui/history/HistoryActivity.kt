package com.osmanabdelsalam.calculator2app.ui.history

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.RecyclerView
import com.osmanabdelsalam.calculator2app.R
import com.osmanabdelsalam.calculator2app.databinding.ActivityHistoryBinding
import kotlin.Deprecated

@Suppress("DEPRECATION")
class HistoryActivity : AppCompatActivity() {
    private val viewModel: HistoryActivityViewModel by viewModels()

    private val rvHistoryList: RecyclerView by lazy {
        mBinding.rvHistoryListItems
    }
    private val adapter: HistoryRecyclerAdapter by lazy {
        HistoryRecyclerAdapter()
    }

    companion object {
        const val EQUATION_KEY = "EQUATION"
        const val EQUATION_FOR_DISPLAY_PANEL_KEY = "EQUATION_FOR_DISPLAY_PANEL_KEY"
        const val EQUATION_RESULT_KEY = "EQUATION_RESULT"
    }
    private lateinit var mBinding: ActivityHistoryBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        rvHistoryList.adapter = adapter
        viewModel.historyList.observe(this) {
            adapter.submitList(it)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteItem(adapter.currentList[viewHolder.adapterPosition])
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }).attachToRecyclerView(rvHistoryList)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val returnIntent = Intent()
                val selectedItem = adapter.currentList[viewHolder.adapterPosition].text
                val equation = selectedItem.substring(0, selectedItem.indexOf("="))
                val result = selectedItem.substring(selectedItem.indexOf("=")+1, selectedItem.lastIndex+1)
                returnIntent.putExtra(EQUATION_KEY, equation.replace('X', '*', true))
                returnIntent.putExtra(EQUATION_RESULT_KEY, result)
                returnIntent.putExtra(EQUATION_FOR_DISPLAY_PANEL_KEY, equation)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }).attachToRecyclerView(rvHistoryList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.history_menu, menu)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.clearMenuItem -> {
                viewModel.clearItems()
                adapter.notifyDataSetChanged()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }
}