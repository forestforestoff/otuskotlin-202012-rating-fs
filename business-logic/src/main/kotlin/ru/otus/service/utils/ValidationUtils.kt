package ru.otus.service.utils

import ru.otus.cor.validation
import ru.otus.model.context.ExchangeContext

val idNotBlankValidation = validation<ExchangeContext> {
    validate { rating.id.isNotBlank() }
    withMessage("Rating ID should not be empty")
}

val voteUpdateValidation = validation<ExchangeContext> {
    validate { vote.ratingId.isNotBlank() || vote.value > 0 }
    withMessage("Invalid vote request update")
}
