package ru.otus.model

data class PrincipalModel(
    val id: String = "",
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: List<UserGroups> = emptyList()
) {

    companion object {
        val NONE = PrincipalModel()
    }
}
