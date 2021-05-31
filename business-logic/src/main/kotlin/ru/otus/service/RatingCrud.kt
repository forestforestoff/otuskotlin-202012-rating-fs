package ru.otus.service

import ru.otus.model.context.ExchangeContext
import ru.otus.service.pipelines.RatingCreate
import ru.otus.service.pipelines.RatingDelete
import ru.otus.service.pipelines.RatingRead
import ru.otus.service.pipelines.RatingUpdate

object RatingCrud {

    suspend fun ExchangeContext.create() {
        RatingCreate.execute(this)
    }

    suspend fun ExchangeContext.read() {
        RatingRead.execute(this)
    }

    suspend fun ExchangeContext.update() {
        RatingUpdate.execute(this)
    }

    suspend fun ExchangeContext.delete() {
        RatingDelete.execute(this)
    }
}
