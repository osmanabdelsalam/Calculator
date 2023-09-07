package com.osmanabdelsalam.calculator2app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osmanabdelsalam.arithmetic.BadSyntaxException
import com.osmanabdelsalam.arithmetic.BaseNotFoundException
import com.osmanabdelsalam.arithmetic.DomainException
import com.osmanabdelsalam.arithmetic.ExpressionParser
import com.osmanabdelsalam.arithmetic.ImaginaryException
import com.osmanabdelsalam.calculator2app.repositories.HistoryRepository
import java.text.DecimalFormat

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    private val historyRepository: HistoryRepository by lazy { HistoryRepository(application) }

    private val _result by lazy { MutableLiveData<String>() }
    private val _equation by lazy { MutableLiveData<String>() }

    val result: LiveData<String> = _result
    val equation: LiveData<String> = _equation

    init {
        _equation.value = ""
        _result.value = ""
    }

    fun replaceEquationContent(newContent: String) {
        _equation.value = newContent
    }

    fun replaceResultContent(newResultContent: String) {
        _result.value = newResultContent
    }

    fun appendToEquation(item: String) {

        try {

            if(arrayOf("+", "-", "/", "x", "X", ".").contains(item)) {
                if(arrayOf("+", "-", "/", "x", "X", ".").contains(_equation!!.value!!.last().toString())) {
                    _equation.value = _equation.value?.substring(0, _equation.value!!.lastIndex)+item
                } else if(!_equation.value!!.endsWith(item, true)) {
                    _equation.value = _equation.value+item
                }
            } else {
                _equation.value = _equation.value+item
            }
        } catch (e: NullPointerException) {}
        _result.value = ""
    }

    fun removeLastEquationItem() {
        try {
            if(_equation.value!!.isNotEmpty()) {
                _equation.value = _equation!!.value!!.substring(0, _equation!!.value!!.lastIndex)
            }
        } catch (e: NullPointerException) {}
        _result.value = ""
    }

    fun clearEquation() {
        _equation.value = ""
        _result.value = ""
    }

    fun calculateEquation() {
        if(_equation.value?.isNotEmpty() == true) {
            try {
                val format = DecimalFormat("0.#")
                val parser = ExpressionParser()
                val parserResult =
                    format.format(parser.evaluate(_equation.value?.replace("X", "*")?.replace("x", "*") ?: ""))
                _result.value = parserResult.toString()
                historyRepository.insertEntry(_equation.value + "=" + parserResult.toString())
            } catch (e: BadSyntaxException) {

            } catch (e: DomainException) {

            } catch (e: ImaginaryException) {

            } catch (e: BaseNotFoundException) {

            } catch (e: Exception) { }
        }
    }
}