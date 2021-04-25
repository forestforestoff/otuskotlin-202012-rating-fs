package ru.otus.model.context

data class ProjectError(
    override val message: String = "",
    override val code: String = "",
    override val group: IError.Group = IError.Group.NONE,
    override val field: String = "",
    override val level: IError.Level = IError.Level.ERROR
) : IError
