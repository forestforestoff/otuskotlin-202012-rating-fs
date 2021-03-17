package ru.otus.model

import java.time.LocalDateTime

data class Vote(
    override val id: String = "",
    val voterId: String = "",
    val value: Int = -1,
    val voteDateTime: LocalDateTime = LocalDateTime.now()
) : IRatingId {

    companion object {
        val NONE = Vote()
    }
}
