package ru.otus.model

data class Vote(
    override val id: String,
    override val groupId: String,
    val value: Int = -1,
    val voterId: String = ""
) : IRatingId, IRatingGroupId
