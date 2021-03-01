package ru.otus.model

import java.time.LocalDateTime

data class Vote(
    override val id: String = "",
    override val groupId: String = "",
    val voterId: String = "",
    val value: Int = -1,
    val voteTime: LocalDateTime = LocalDateTime.now()
) : IRatingId, IRatingGroupId
