package ru.otus.cor

class ValidationException(val failMessage: String) : RuntimeException(failMessage)
