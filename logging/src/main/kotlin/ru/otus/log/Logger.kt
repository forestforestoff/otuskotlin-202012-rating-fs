package ru.otus.log

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

fun logger(loggerId: String): LogContext = logger(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun logger(clazz: Class<out Any>): LogContext = logger(
    logger = LoggerFactory.getLogger(clazz) as Logger
)

/**
 * Функция генерирует внутренний "логер" - объект класса MpLogContext
 *
 * @param logger интстанс логера Logback, полученный методом [[LoggerFactory.getLogger()]]
 */
fun logger(logger: Logger): LogContext = LogContext(
    logger = logger,
    loggerId = logger.name,
)
