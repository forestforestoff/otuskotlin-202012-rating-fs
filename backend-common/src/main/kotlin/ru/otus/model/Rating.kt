package ru.otus.model

import java.time.LocalDateTime

data class Rating(
    override val id: String = "",
    override val groupId: String = "",
    val value: Double = 0.0,
    val voterIdAndTime: Map<String, LocalDateTime> = emptyMap()
): IRatingId, IRatingGroupId
