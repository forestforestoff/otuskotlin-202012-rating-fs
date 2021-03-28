package ru.otus.cor

typealias Predicate<T> = suspend T.() -> Boolean

typealias Runnable<T> = suspend T.() -> Unit

typealias ValidationHandler<T> = suspend T.(ValidationException) -> Unit

typealias ErrorHandler<T> = suspend T.(Throwable) -> Unit
