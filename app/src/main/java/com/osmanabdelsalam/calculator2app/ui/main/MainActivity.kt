package com.osmanabdelsalam.calculator2app.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.osmanabdelsalam.arithmetic.BadSyntaxException
import com.osmanabdelsalam.arithmetic.BaseNotFoundException
import com.osmanabdelsalam.arithmetic.DomainException
import com.osmanabdelsalam.arithmetic.ExpressionParser
import com.osmanabdelsalam.arithmetic.ImaginaryException
import com.osmanabdelsalam.calculator2app.R
import com.osmanabdelsalam.calculator2app.databinding.ActivityMainBinding
import com.osmanabdelsalam.calculator2app.db.historydb.HistoryContract
import com.osmanabdelsalam.calculator2app.ui.history.HistoryActivity
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val numbersPanelString: StringBuilder = StringBuilder("")
    private val normalOperators = arrayOf(
        "+",
        "-",
        "/",
        "*"
    )

    private val expressionParser: ExpressionParser by lazy {
        ExpressionParser()
    }

    private val btnNumbers: List<Button> by lazy {
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
            mBinding.btnNum9
        )
    }

    private val btnOperators: List<Button> by lazy {
        listOf(
            mBinding.btnOprAdd,
            mBinding.btnOprSubtract,
            mBinding.btnOprMultiply,
            mBinding.btnOprDivide
        )
    }

    private val btnDot: Button by lazy {
        mBinding.btnDot
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
        if (intent!!.hasExtra(HistoryActivity.EQUATION_KEY) && intent.hasExtra(HistoryActivity.EQUATION_FOR_DISPLAY_PANEL_KEY)) {
            numbersPanelString.clear()
            mainBinding.tvNumbers.text = intent.getStringExtra(HistoryActivity.EQUATION_FOR_DISPLAY_PANEL_KEY)
            numbersPanelString.append(intent.getStringExtra(HistoryActivity.EQUATION_KEY))
        }

        if(intent.hasExtra(HistoryActivity.EQUATION_RESULT_KEY)) {
            mainBinding.tvResult.text = intent.getStringExtra(HistoryActivity.EQUATION_RESULT_KEY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        tvNumbers.movementMethod = ScrollingMovementMethod()
        tvResult.movementMethod = ScrollingMovementMethod()

        btnNumbers.forEach { btnNumber: Button ->
            btnNumber.setOnClickListener {
            if(it is Button) {
                    val number = it.text!!.toString()
                    numbersPanelString.append(number)
                    tvNumbers.append(number)
                }
            }
        }

        btnOperators.forEach {btnOperator: Button ->
            btnOperator.setOnClickListener {
                if(it is Button) {
                    val operator = it.text!!.toString()
                    normalOperators.forEach { operatorView ->
                        if(numbersPanelString.isNotEmpty() && operatorView == numbersPanelString[numbersPanelString.lastIndex].toString()) {
                            val tvNumbersValue = tvNumbers.text
                            tvNumbers.text = tvNumbersValue.substring(0, tvNumbersValue.lastIndex)
                            numbersPanelString.deleteCharAt(numbersPanelString.lastIndex)
                        }
                    }
                    if(numbersPanelString.isEmpty() && (operator == "x" || operator == "X" || operator == "+" || operator == "/")) return@setOnClickListener
                    if(operator == "X" || operator == "x") {
                        numbersPanelString.append("*")
                    } else {
                        numbersPanelString.append(operator)
                    }
                    tvNumbers.append(operator)
                }
            }
        }

        btnClear.setOnClickListener {
            tvResult.text = numbersPanelString.clear().toString()
            tvNumbers.text = numbersPanelString.toString()
        }

        btnBackspace.apply {
            setOnClickListener {
                tvResult.text = ""
                tvNumbers.text = removeLast(tvNumbers.text.toString())
                try {
                    numbersPanelString.deleteCharAt(numbersPanelString.lastIndex)
                } catch (_: Exception) {

                }
            }

            setOnLongClickListener {
                tvResult.text = ""
                tvNumbers.text =""
                numbersPanelString.clear()
                true
            }
        }

        btnDot.setOnClickListener {
            tvNumbers.append(".")
            numbersPanelString.append(".")
        }

        btnEqual.setOnClickListener {
            if(numbersPanelString.isNotEmpty()) {
                try {
                    val format = DecimalFormat("0.#")
                    tvResult.text = format.format(expressionParser.evaluate(numbersPanelString.toString()))
                    val history = HistoryContract.HistoryDbHelper(this)
                    history.insertEntry("${tvNumbers.text}=${tvResult.text}")
                } catch (e: BadSyntaxException) {
                    tvResult.text = getString(R.string.error)
                } catch (e: DomainException) {
                    tvResult.text = getString(R.string.error)
                } catch (e: ImaginaryException) {
                    tvResult.text = getString(R.string.error)
                } catch (e: BaseNotFoundException) {
                    tvResult.text = getString(R.string.error)
                } catch (e: Exception) {
                    tvResult.text = getString(R.string.error)
                }
            }
        }

    }

    private fun removeLast(text: String): String {
        return if(text.isEmpty()) "" else text.substring(0, text.lastIndex)
    }

    override fun onStart() {
        super.onStart()
        bindPersistData()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bindPersistData()
    }

    override fun onPause() {
        super.onPause()
        Log.i("AppLifecycle", "onPause: ")
        val sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE) ?: return
        with(sharedPrefs.edit()) {
            val stringToPersist = StringBuilder()
            stringToPersist.append(tvNumbers.text)
            putString(getString(R.string.last_equation_value), "${tvNumbers.text}=${tvResult.text}")
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

    private fun bindPersistData() {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        sharedPref.getString(getString(R.string.last_equation_value), "")?.apply {
            if(isNotEmpty()) {
                val equation = substring(0, indexOf('='))
                val result = substring(indexOf('=')+1, lastIndex+1)
                numbersPanelString.append(equation.replace('X', '*', ignoreCase = true))
                tvNumbers.text = equation
                tvResult.text = result
            }
        }
    }

    private fun launchHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        mGetHistoryActivity.launch(intent)
    }

}