package ru.otus.model.context

import ru.otus.model.PrincipalModel
import ru.otus.model.Rating
import ru.otus.model.Vote
import java.time.Instant
import java.util.*

data class ExchangeContext(
    val created: Instant = Instant.now(),
    val contextUid: UUID = UUID.randomUUID(),
    var status: ContextStatus = ContextStatus.INIT,
    var errors: MutableList<IError> = mutableListOf(),
    val userSession: ISessionWrapper<*> = EmptySession(),
    var principalModel: PrincipalModel = PrincipalModel.NONE,
    var useAuth: Boolean = true,

    var rating: Rating = Rating.NONE,
    var vote: Vote = Vote.NONE
)
