package com.my.mycalculator

class CalculatorContract {

  sealed class Event {
    data class EnteredDial(val dial: Dial) : Event()
    data object Delete : Event()
  }

  data class State(
    val expression: String = "",
    var isCalculationOver: Boolean = false
  )
}