package com.my.mycalculator

data class Dial(
  val symbol: Char,
  val isOperator: Boolean = false
)

const val point = '.'
const val empty = '#'
const val deleteButton = '<'

val dials = listOf(
  listOf(
    Dial(symbol = Operators.C.symbol, isOperator = true),
    Dial(symbol = empty),
    Dial(symbol = empty),
    Dial(symbol = Operators.Divide.symbol, isOperator = true),
  ),
  listOf(
    Dial(symbol = '7'),
    Dial(symbol = '8'),
    Dial(symbol = '9'),
    Dial(symbol = Operators.Multiply.symbol, isOperator = true),
  ),
  listOf(
    Dial(symbol = '4'),
    Dial(symbol = '5'),
    Dial(symbol = '6'),
    Dial(symbol = Operators.Subtract.symbol, isOperator = true),
  ),
  listOf(
    Dial(symbol = '1'),
    Dial(symbol = '2'),
    Dial(symbol = '3'),
    Dial(symbol = Operators.Plus.symbol, isOperator = true),
  ),
  listOf(
    Dial(symbol = deleteButton),
    Dial(symbol = '0'),
    Dial(symbol = point),
    Dial(symbol = Operators.Equals.symbol, isOperator = true),
  )
)