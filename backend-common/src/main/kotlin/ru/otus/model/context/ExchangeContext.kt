package ru.otus.model.context

import ru.otus.model.IError
import ru.otus.model.Rating
import ru.otus.model.Vote

data class ExchangeContext(
    var status: ContextStatus = ContextStatus.NONE,
    var errors: MutableList<IError> = mutableListOf(),

    var rating: Rating = Rating.NONE,
    var vote: Vote = Vote.NONE
)
