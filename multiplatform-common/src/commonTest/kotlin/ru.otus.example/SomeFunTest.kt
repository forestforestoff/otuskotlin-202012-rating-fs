package ru.otus.example

import kotlin.test.Test
import kotlin.test.assertTrue

class SomeFunTest {
    @Test
    fun someFunTest() {
        val str = "TestString"
        assertTrue("Result contains parameter string") {
            someFun(str).contains(str)
        }
    }
}
