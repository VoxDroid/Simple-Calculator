@file:Suppress("DEPRECATION")

package com.drei.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.allClear
import kotlinx.android.synthetic.main.activity_main.btnBackspace
import kotlinx.android.synthetic.main.activity_main.btnDivide
import kotlinx.android.synthetic.main.activity_main.btnDot
import kotlinx.android.synthetic.main.activity_main.btnEight
import kotlinx.android.synthetic.main.activity_main.btnEqual
import kotlinx.android.synthetic.main.activity_main.btnFive
import kotlinx.android.synthetic.main.activity_main.btnFour
import kotlinx.android.synthetic.main.activity_main.btnMinus
import kotlinx.android.synthetic.main.activity_main.btnMultiply
import kotlinx.android.synthetic.main.activity_main.btnNine
import kotlinx.android.synthetic.main.activity_main.btnOne
import kotlinx.android.synthetic.main.activity_main.btnPLus
import kotlinx.android.synthetic.main.activity_main.btnSeven
import kotlinx.android.synthetic.main.activity_main.btnSix
import kotlinx.android.synthetic.main.activity_main.btnThree
import kotlinx.android.synthetic.main.activity_main.btnTwo
import kotlinx.android.synthetic.main.activity_main.btnZero
import kotlinx.android.synthetic.main.activity_main.clear
import kotlinx.android.synthetic.main.activity_main.tvCurrentOperand
import kotlinx.android.synthetic.main.activity_main.tvInput
import kotlinx.android.synthetic.main.activity_main.tvOldInput
import java.math.BigDecimal
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var currentInput = StringBuilder()
    private var currentOperator = Operator.NONE
    private var operand1: BigDecimal? = null
    enum class Operator {
        NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnOne.setOnClickListener { appendNumber("1") }
        btnTwo.setOnClickListener { appendNumber("2") }
        btnThree.setOnClickListener { appendNumber("3") }
        btnFour.setOnClickListener { appendNumber("4") }
        btnFive.setOnClickListener { appendNumber("5") }
        btnSix.setOnClickListener { appendNumber("6") }
        btnSeven.setOnClickListener { appendNumber("7") }
        btnEight.setOnClickListener { appendNumber("8") }
        btnNine.setOnClickListener { appendNumber("9") }
        btnZero.setOnClickListener { appendNumber("0") }
        btnDot.setOnClickListener { appendNumber(".") }

        btnPLus.setOnClickListener { setOperator(Operator.ADD) }
        btnMinus.setOnClickListener { setOperator(Operator.SUBTRACT) }
        btnMultiply.setOnClickListener { setOperator(Operator.MULTIPLY) }
        btnDivide.setOnClickListener { setOperator(Operator.DIVIDE) }

        btnEqual.setOnClickListener { calculateResult() }

        clear.setOnClickListener { clearInput() }
        allClear.setOnClickListener { allClearInput() }

        btnBackspace.setOnClickListener { handleBackspace() }
        btnEqual.setOnClickListener { calculateResult() }
    }

    private fun appendNumber(number: String) {
        currentInput.append(number)
        updateDisplay()
    }

    private fun handleBackspace() {
        if (currentInput.isNotEmpty()) {
            currentInput.deleteCharAt(currentInput.length - 1)
            updateDisplay()
        }
    }

    private fun setOperator(operator: Operator) {
        if (tvInput.text == "0") {
            return
        }

        if (operand1 == null) {

            operand1 = BigDecimal(currentInput.toString())
            currentInput.clear()

            tvOldInput.text = operand1.toString()
            tvInput.text = ""
        }

        currentOperator = operator
        tvCurrentOperand.text = operatorToString(operator)
    }

    private fun operatorToString(operator: Operator): String {
        return when (operator) {
            Operator.ADD -> "+"
            Operator.SUBTRACT -> "-"
            Operator.MULTIPLY -> "×"
            Operator.DIVIDE -> "÷"
            Operator.NONE -> ""
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateResult() {
        if (operand1 == null || currentOperator == Operator.NONE || currentInput.isEmpty()) {
            return
        }

        val operand2 = BigDecimal(currentInput.toString())
        var result: BigDecimal? = null

        when (currentOperator) {
            Operator.ADD -> result = operand1?.add(operand2)
            Operator.SUBTRACT -> result = operand1?.subtract(operand2)
            Operator.MULTIPLY -> result = operand1?.multiply(operand2)
            Operator.DIVIDE -> {
                if (operand2 != BigDecimal.ZERO) {
                    result = operand1?.divide(operand2, 2, BigDecimal.ROUND_HALF_UP)
                } else {
                    tvInput.text = "Invalid."
                    resetState()
                    return
                }
            }
            Operator.NONE -> result = operand2
        }

        if (result != null) {

            if (operand1 != null) {
                if (currentOperator != Operator.NONE) {
                    tvOldInput.text = ("$operand1 ${operatorToString(currentOperator)} $operand2").toString()
                } else {
                    tvOldInput.text = operand1.toString()
                }
            } else {
                tvOldInput.text = ""
            }

            val formattedResult = if (result.stripTrailingZeros().scale() <= 0) {
                result.setScale(0, BigDecimal.ROUND_HALF_UP).toString()
            } else {
                result.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
            }



            tvInput.text = formattedResult

            operand1 = result

            resetState()
        }
    }

    private fun resetState() {
        currentInput.clear()
        currentOperator = Operator.NONE
    }

    private fun allClearInput() {
        currentInput.clear()
        operand1 = null
        currentOperator = Operator.NONE
        tvOldInput.text = ""
        tvInput.text = "0"
        tvCurrentOperand.text = ""
    }

    private fun clearInput() {
        currentInput.clear()
        currentOperator = Operator.NONE
        tvInput.text = "0"
        operand1 = null
    }

    private fun updateDisplay() {
        tvInput.text = currentInput.toString()
    }
}