package ru.otus.model

data class Rating(
    override val id: String = "",
    override val groupId: String = "",
    val value: Double = 0.0,
    val votes: List<Vote> = emptyList()
) : IRatingId, IRatingGroupId
