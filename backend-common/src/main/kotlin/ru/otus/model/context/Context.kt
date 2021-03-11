package ru.otus.model.context

import ru.otus.model.Rating
import ru.otus.model.Vote

data class Context(
    var rating: Rating = Rating.NONE,
    var vote: Vote = Vote.NONE
)
