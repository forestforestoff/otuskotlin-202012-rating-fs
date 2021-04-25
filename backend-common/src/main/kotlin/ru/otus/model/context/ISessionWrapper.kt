package ru.otus.model.context

interface ISessionWrapper<T> {
    val specificSession: T
}

class EmptySession(override val specificSession: String = "Empty session") : ISessionWrapper<String>
