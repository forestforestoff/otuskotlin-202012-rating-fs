package ru.otus.model

data class Rating(
    override val id: String = "",
    override val groupId: String = "",
    val votes: List<Vote> = emptyList(),
    val value: Double = 0.0
) : IModelIdentity, IRatingGroupId {

    companion object {
        val NONE = Rating()
    }
}
