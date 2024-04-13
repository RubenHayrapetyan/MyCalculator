package com.my.mycalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

  var state by mutableStateOf(CalculatorContract.State())
    private set

  fun onEvent(event: CalculatorContract.Event) {
    when (event) {
      is CalculatorContract.Event.EnteredDial -> checkDial(event.dial)
      is CalculatorContract.Event.Delete -> delete()
    }
  }

  private fun calculateExpression(expression: String): Double? {
    try {
      val expressionWithoutSpaces = expression.replace("\\s".toRegex(), "")

      val numbers = mutableListOf<Double>()
      val operators = mutableListOf<Char>()

      var numStr = ""
      var lastCharWasOperator = true
      for (char in expressionWithoutSpaces) {
        when {
          char.isDigit() || (char == point && numStr.isNotEmpty()) -> numStr += char
          char == Operators.Subtract.symbol && lastCharWasOperator -> numStr += char
          else -> {
            if (numStr.isNotEmpty()) {
              numbers.add(numStr.toDouble())
              numStr = ""
            }
            operators.add(char)
            lastCharWasOperator = true
          }
        }
        if (char != Operators.Subtract.symbol) {
          lastCharWasOperator = false
        }
      }
      if (numStr.isNotEmpty()) {
        numbers.add(numStr.toDouble())
      }

      var index = 0
      while (index < operators.size) {
        if (operators[index] == Operators.Multiply.symbol || operators[index] == Operators.Divide.symbol) {
          val result = when (operators[index]) {
            Operators.Multiply.symbol -> numbers[index] * numbers[index + 1]
            Operators.Divide.symbol -> numbers[index] / numbers[index + 1]
            else -> throw IllegalArgumentException("Invalid operator")
          }
          numbers[index] = result
          numbers.removeAt(index + 1)
          operators.removeAt(index)
        } else {
          index++
        }
      }

      var result = numbers[0]
      for (i in 0 until operators.size) {
        result = when (operators[i]) {
          Operators.Plus.symbol -> result + numbers[i + 1]
          Operators.Subtract.symbol -> result - numbers[i + 1]
          else -> throw IllegalArgumentException("Invalid operator") // Throw an exception for unexpected operator
        }
      }

      return result
    } catch (e: Exception) {
      return null
    }
  }

  private fun checkDecimal(num: Double): String {
    return if (num - num.toInt() > 0) {
      num.toString()
    } else {
      num.toLong().toString()
    }
  }

  private fun checkDial(dial: Dial) {
    state = if (!dial.isOperator && state.isCalculationOver && dial.symbol != point) {
      CalculatorContract.State()
    } else {
      state.copy(isCalculationOver = false)
    }
    val expression = state.expression
    val fullExpression = expression + dial.symbol

    if (!isValidDoubleNumber(expression = "$expression${dial.symbol}")) {
      return
    }
    if (!isValidExpression(fullExpression)) {
      return
    }

    when (dial.symbol) {
      Operators.Equals.symbol -> {
        val calculatedResult: Double? = calculateExpression(state.expression)
        if (calculatedResult == null) {
          CalculatorContract.State()
        } else {
          val resultExpression: String = checkDecimal(calculatedResult)
          state = state.copy(expression = resultExpression, isCalculationOver = true)
        }
        return
      }

      Operators.C.symbol -> {
        state = CalculatorContract.State()
        return
      }
    }

    if (expression.isEmpty() &&
      (dial.symbol == point || (dial.isOperator && dial.symbol != Operators.Subtract.symbol))
    ) {
      return
    }
    if (expression.isNotEmpty() && !expression.last().isDigit() && dial.isOperator) {
      delete()
    }
    if (expression.isNotEmpty() && dial.symbol == point && !expression.last().isDigit()) {
      return
    }

    val updatedExpression = "${state.expression}${dial.symbol}"
    state = state.copy(expression = updatedExpression)
  }

  private fun delete() {
    val expression = state.expression
    val updatedExpression = if (expression.isNotEmpty()) expression.dropLast(1) else expression
    state = state.copy(expression = updatedExpression)
  }

  private fun isValidDoubleNumber(expression: String): Boolean {
    var decimalPointCount = 0
    var isLastCharDigit = false

    for (char in expression) {
      when {
        char.isDigit() -> {
          if (!isLastCharDigit) {
            isLastCharDigit = true
            decimalPointCount = 0
          }
        }

        char == point -> {
          decimalPointCount++
          if (decimalPointCount > 1) {
            return false
          }
        }

        else -> isLastCharDigit = false
      }
    }
    return true
  }

  private fun isValidExpression(expression: String): Boolean {
    val numbers = expression.split("[+\\-*/]".toRegex())
    for (number in numbers) {
      if (!isValidNumber(number)) {
        return false
      }
    }
    return true
  }

  private fun isValidNumber(number: String): Boolean {
    if (number.matches("^0[0-9]+(\\.[0-9]+)?".toRegex()) && !number.matches("^0\\.[0-9]+".toRegex())) {
      return false
    }
    return number.count { it == point } <= 1
  }
}
