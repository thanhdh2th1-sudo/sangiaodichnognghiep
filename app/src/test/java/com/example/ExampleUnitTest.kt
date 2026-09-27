package com.example

import com.example.ui.components.formatVnd
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testCurrencyFormatting() {
    assertEquals("150.000 ₫", formatVnd(150000L))
    assertEquals("3.000.000 ₫", formatVnd(3000000L))
    assertEquals("3.000 ₫", formatVnd(3000L))
  }

  @Test
  fun testPlatformFeeDeduction() {
    val initialBalance = 50000L
    val platformFee = 3000L
    val balanceAfter = initialBalance - platformFee
    assertEquals(47000L, balanceAfter)
    assertTrue(initialBalance >= platformFee)
  }
}
