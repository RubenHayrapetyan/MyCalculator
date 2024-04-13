package com.my.mycalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.mycalculator.ui.theme.MyCalculatorTheme

@Composable
fun CalculatorScreen(
  state: CalculatorContract.State,
  onEvent: (CalculatorContract.Event) -> Unit
) {

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = colorResource(id = R.color.calculator_background))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(3f)
        .background(color = colorResource(id = R.color.white)),
    ) {
      println(state.expression)
      Text(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .align(Alignment.CenterEnd),
        text = state.expression,
        textAlign = TextAlign.End,
        fontSize = 32.sp,
        color = colorResource(id = R.color.black),
      )
    }

    dials.forEach { rowSymbols ->
      Row(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        rowSymbols.forEach { dial ->
          when (dial.symbol) {
            deleteButton -> {
              DeleteButton(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxHeight(),
                onClick = {
                  onEvent(CalculatorContract.Event.Delete)
                }
              )
            }
            empty -> {
              Spacer(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxHeight()
              )
            }
            else -> {
              CalculatorButton(
                dial = dial,
                modifier = Modifier
                  .weight(1f)
                  .fillMaxHeight()
              ) {
                onEvent(
                  CalculatorContract.Event.EnteredDial(
                    dial = dial
                  )
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CalculatorButton(
  dial: Dial,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val dialBackgroundColor by remember {
    mutableIntStateOf(
      if (dial.isOperator) {
        if (dial.symbol == Operators.Equals.symbol) {
          R.color.equal_background
        } else {
          R.color.operator_background
        }
      } else {
        R.color.white
      }
    )
  }
  val dialTextColor by remember {
    mutableIntStateOf(
      if (dial.symbol == Operators.Equals.symbol) R.color.white
      else R.color.black
    )
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .padding(8.dp)
      .clip(CircleShape)
      .clickable { onClick() }
      .background(colorResource(id = dialBackgroundColor))
      .aspectRatio(1f)
  ) {
    Text(
      text = dial.symbol.toString(),
      fontSize = 34.sp,
      color = colorResource(id = dialTextColor)
    )
  }
}

@Composable
fun DeleteButton(
  modifier: Modifier,
  onClick: () -> Unit
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .padding(8.dp)
      .clip(CircleShape)
      .clickable { onClick() }
      .background(colorResource(id = R.color.white))
      .aspectRatio(1f)
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_delete),
      colorFilter = ColorFilter.tint(colorResource(id = R.color.black)),
      contentDescription = null
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
  MyCalculatorTheme {
    CalculatorScreen(
      state = CalculatorContract.State(),
      onEvent = {}
    )
  }
}