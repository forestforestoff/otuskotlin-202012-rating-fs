package ru.otus.cor

interface IOperation<T> {
    suspend fun execute(context: T)
}
