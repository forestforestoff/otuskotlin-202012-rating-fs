package ru.otus.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

private val jackson = ObjectMapper().registerKotlinModule()

fun <T> fromJson(value: String, clazz: Class<T>): T = jackson.readValue(value, clazz)

fun toJson(value: Any): String = jackson.writeValueAsString(value)
