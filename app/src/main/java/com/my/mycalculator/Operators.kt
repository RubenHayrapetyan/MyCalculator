package com.my.mycalculator

sealed class Operators (val symbol : Char) {
  data object Plus : Operators('+')
  data object Subtract : Operators('-')
  data object Multiply : Operators('x')
  data object Divide : Operators('/')
  data object Equals : Operators('=')
  data object C : Operators('C')
}
