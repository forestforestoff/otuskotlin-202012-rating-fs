package ru.otus.repositories

import ru.otus.model.context.ExchangeContext

interface IRatingRepository {
    suspend fun ExchangeContext.read()
    suspend fun ExchangeContext.create()
    suspend fun ExchangeContext.update()
    suspend fun ExchangeContext.delete()

    companion object {
        val NONE = object : IRatingRepository {
            override suspend fun ExchangeContext.read() {
                TODO("Not yet implemented")
            }

            override suspend fun ExchangeContext.create() {
                TODO("Not yet implemented")
            }

            override suspend fun ExchangeContext.update() {
                TODO("Not yet implemented")
            }

            override suspend fun ExchangeContext.delete() {
                TODO("Not yet implemented")
            }
        }
    }
}
