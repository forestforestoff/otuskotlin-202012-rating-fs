package ru.otus.cor

class Pipeline<T>
private constructor(
    private val operations: Collection<IOperation<T>>,
    private val checkPrecondition: Predicate<T>,
    private val handleValidationFail: ValidationHandler<T>,
    private val handleError: ErrorHandler<T>
) : IOperation<T> {
    override suspend fun execute(context: T) {
        try {
            if (checkPrecondition(context)) operations.forEach { it.execute(context) }
        } catch (validationException: ValidationException) {
            handleValidationFail(context, validationException)
        } catch (throwable: Throwable) {
            handleError(context, throwable)
        }
    }

    @PipelineDsl
    class Builder<T> {
        private val operations: MutableList<IOperation<T>> = mutableListOf()
        private var checkPrecondition: Predicate<T> = { true }
        private var handleValidationFail: ValidationHandler<T> = { throw it }
        private var handleError: ErrorHandler<T> = { throw it }

        fun execute(operation: IOperation<T>) {
            operations.add(operation)
        }

        fun execute(block: Runnable<T>) {
            execute(Operation.Builder<T>().apply { execute(block) }.build())
        }

        fun validate(validation: IValidation<T>) {
            operations.add(validation)
        }

        fun startIf(block: Predicate<T>) {
            checkPrecondition = block
        }

        fun handleValidationFail(block: ValidationHandler<T>) {
            handleValidationFail = block
        }

        fun onError(block: ErrorHandler<T>) {
            handleError = block
        }

        fun build(): Pipeline<T> = Pipeline(operations, checkPrecondition, handleValidationFail, handleError)
    }
}
