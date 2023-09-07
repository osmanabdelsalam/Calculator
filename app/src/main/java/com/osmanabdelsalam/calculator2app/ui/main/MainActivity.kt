package com.osmanabdelsalam.calculator2app.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.osmanabdelsalam.calculator2app.R
import com.osmanabdelsalam.calculator2app.databinding.ActivityMainBinding
import com.osmanabdelsalam.calculator2app.ui.history.HistoryActivity


class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var mBinding: ActivityMainBinding

    private val buttons: List<Button> by lazy {
        listOf(
            mBinding.btnNum0,
            mBinding.btnNum1,
            mBinding.btnNum2,
            mBinding.btnNum3,
            mBinding.btnNum4,
            mBinding.btnNum5,
            mBinding.btnNum6,
            mBinding.btnNum7,
            mBinding.btnNum8,
            mBinding.btnNum9,
            mBinding.btnOprAdd,
            mBinding.btnOprSubtract,
            mBinding.btnOprMultiply,
            mBinding.btnOprDivide,
            mBinding.btnDot
        )
    }

    private val btnBackspace: ImageView by lazy {
        mBinding.btnBackspace
    }

    private val btnEqual: Button by lazy {
        mBinding.btnEqual
    }

    private val btnClear: Button by lazy {
        mBinding.btnClear
    }

    private val tvNumbers: TextView by lazy {
        mBinding.tvNumbers
    }

    private val tvResult: TextView by lazy {
        mBinding.tvResult
    }

    private val mGetHistoryActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val mainBinding = mBinding
        if (Activity.RESULT_OK != it.resultCode) {
            return@registerForActivityResult
        }
        val intent = it.data
        if (intent!!.hasExtra(HistoryActivity.EQUATION)) {
            viewModel.replaceEquationContent(intent.getStringExtra(HistoryActivity.EQUATION) ?: "")
        }

        if(intent.hasExtra(HistoryActivity.EQUATION_RESULT)) {
            viewModel.replaceResultContent(intent.getStringExtra(HistoryActivity.EQUATION_RESULT) ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        tvNumbers.movementMethod = ScrollingMovementMethod()
        tvResult.movementMethod = ScrollingMovementMethod()

        viewModel.equation.observe(this) {
            tvNumbers.text = it
        }

        viewModel.result.observe(this) {
            tvResult.text = it
        }

        buttons.forEach { btnNumber: Button ->
            btnNumber.setOnClickListener {
            if(it is Button) {
                viewModel.appendToEquation(it?.text.toString())
                }
            }
        }

        btnClear.setOnClickListener {
            viewModel.clearEquation()
        }

        btnBackspace.apply {
            setOnClickListener {
                viewModel.removeLastEquationItem()
            }

            setOnLongClickListener {
                viewModel.clearEquation()
                true
            }
        }

        btnEqual.setOnClickListener {
            viewModel.calculateEquation()
        }

    }

    private fun bindPersistData() {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        sharedPref.getString(getString(R.string.last_equation_value), "")?.apply {
            if(isNotEmpty()) {
                val equation = substring(0, indexOf('='))
                val result = substring(indexOf('=')+1, lastIndex+1)
                viewModel.replaceEquationContent(equation)
                viewModel.replaceResultContent(result)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindPersistData()
    }


    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE) ?: return
        with(sharedPrefs.edit()) {
            putString(getString(R.string.last_equation_value), "${viewModel.equation.value}=${viewModel.result.value}")
            apply()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.historyMenuItem -> {
                launchHistoryActivity()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun launchHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        mGetHistoryActivity.launch(intent)
    }
}