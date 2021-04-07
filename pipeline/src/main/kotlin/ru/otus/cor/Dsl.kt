package ru.otus.cor

inline fun <T> pipeline(block: Pipeline.Builder<T>.() -> Unit): Pipeline<T> =
    Pipeline.Builder<T>().apply(block).build()

inline fun <T> operation(block: Operation.Builder<T>.() -> Unit): Operation<T> =
    Operation.Builder<T>().apply(block).build()

inline fun <T> validation(block: Validation.Builder<T>.() -> Unit): Validation<T> =
    Validation.Builder<T>().apply(block).build()

inline fun <T> Pipeline.Builder<T>.pipeline(block: Pipeline.Builder<T>.() -> Unit) {
    execute(Pipeline.Builder<T>().apply(block).build())
}

inline fun <T> Pipeline.Builder<T>.operation(block: Operation.Builder<T>.() -> Unit) {
    execute(Operation.Builder<T>().apply(block).build())
}

inline fun <T> Pipeline.Builder<T>.validation(block: Validation.Builder<T>.() -> Unit) {
    validate(Validation.Builder<T>().apply(block).build())
}
