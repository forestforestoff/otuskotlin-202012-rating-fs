package ru.otus.log

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class LoggerTest {

    @Test
    fun loggerInit() {
        runBlocking {
            val logger = logger(this::class.java)
            logger.doWithLoggingSusp(logId = "test-logger") {
                println("Some action")
            }
        }
    }
}
