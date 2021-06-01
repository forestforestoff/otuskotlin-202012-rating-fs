package ru.otus.service.utils

import ru.otus.cor.PermissionException
import ru.otus.cor.validation
import ru.otus.model.UserGroups
import ru.otus.model.context.ExchangeContext

val idNotBlankValidation = validation<ExchangeContext> {
    validate { rating.id.isNotBlank() }
    withMessage("Rating ID should not be empty")
}

val voteUpdateValidation = validation<ExchangeContext> {
    validate { vote.ratingId.isNotBlank() || vote.value > 0 }
    withMessage("Invalid vote request update")
}

val adminsPermission = validation<ExchangeContext> {
    validate { !useAuth || principalModel.groups.contains(UserGroups.ADMIN) }
    onFail { throw PermissionException("You should have admin permissions to execute this operation") }
}

val moderatorsPermission = validation<ExchangeContext> {
    validate {
        !useAuth ||
                principalModel.groups.contains(UserGroups.MODERATOR) || principalModel.groups.contains(UserGroups.ADMIN)
    }
    onFail { throw PermissionException("You should have moderator permissions to execute this operation") }
}
