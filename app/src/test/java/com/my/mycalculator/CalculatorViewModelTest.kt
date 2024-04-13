package com.my.mycalculator

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorViewModelTest {

  private lateinit var viewModel: CalculatorViewModel
  private val plus = Operators.Plus.symbol
  private val subtract = Operators.Subtract.symbol
  private val multiply = Operators.Multiply.symbol
  private val divide = Operators.Divide.symbol
  private val equals = Operators.Equals.symbol
  private val clear = Operators.C.symbol

  @Before
  fun setUp() {
    viewModel = CalculatorViewModel()
  }

  @Test
  fun `1+5=6 then entering dial (3) must be the new dial`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('1')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))

    assertEquals("3", state.expression)
  }

  @Test
  fun `if 0 point then entering number then point, second point must not be entered`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('2')))

    assertEquals("0${point}532", state.expression)
  }

  @Test
  fun `if 0 point then entering point must not be entered second time`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))

    assertEquals("0$point", state.expression)
  }

  @Test
  fun `if no number entering point must be nothing`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))

    assertEquals("", state.expression)
  }

  @Test
  fun `if text is empty enter two times 0 must be one zero`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))

    assertEquals("0", state.expression)
  }

  @Test
  fun `test delete two times 5+3`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))
    onEvent(CalculatorContract.Event.Delete)
    onEvent(CalculatorContract.Event.Delete)

    assertEquals("5", state.expression)
  }

  @Test
  fun testAddition() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))

    assertEquals("8", state.expression)
  }

  @Test
  fun testSubtraction() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('2')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(subtract)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))

    assertEquals("-1", state.expression)
  }

  @Test
  fun testMultiplication() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(multiply)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('2')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))

    assertEquals("10", state.expression)
  }

  @Test
  fun testDivision() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('8')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(divide)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('2')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))

    assertEquals("4", state.expression)
  }

  @Test
  fun testClear() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('6')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(clear)))

    assertEquals("", state.expression)
  }

  @Test
  fun `test multiple actions 56+80x30+0,5-102`() = with(viewModel) {
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('6')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('8')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(multiply)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('3')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(plus)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(point)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('5')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(subtract)))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('1')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('0')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial('2')))
    onEvent(CalculatorContract.Event.EnteredDial(Dial(equals)))

    assertEquals("2354.5", state.expression)
  }
}