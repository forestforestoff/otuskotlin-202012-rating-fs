package ru.otus.cor

class Validation<T>
private constructor(
    private val validate: Predicate<T>,
    private val onFail: Runnable<T>
) : IValidation<T> {
    override suspend fun execute(context: T) {
        if (!validate(context)) onFail(context)
    }

    @PipelineDsl
    class Builder<T> {
        private var validate: Predicate<T> = { true }
        private var failMessage: String = "Validation error"
        private var onFail: Runnable<T> = { throw ValidationException(failMessage) }

        fun validate(block: Predicate<T>) {
            validate = block
        }

        fun withMessage(validationFailMessage: String) {
            failMessage = validationFailMessage
        }

        fun onFail(block: Runnable<T>) {
            onFail = block
        }

        fun build(): Validation<T> = Validation(validate, onFail)
    }
}
