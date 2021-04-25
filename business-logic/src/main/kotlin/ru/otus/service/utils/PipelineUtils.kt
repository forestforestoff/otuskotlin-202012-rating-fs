package ru.otus.service.utils

import ru.otus.cor.operation
import ru.otus.cor.pipeline
import ru.otus.model.context.ContextStatus
import ru.otus.model.context.ExchangeContext

val completePipeline = pipeline<ExchangeContext> {
    operation {
        startIf { status in setOf(ContextStatus.RUNNING, ContextStatus.FINISHING) }
        execute { status = ContextStatus.SUCCESS }
    }
    operation {
        startIf { status != ContextStatus.SUCCESS }
        execute { status = ContextStatus.ERROR }
    }
}

val runningPipeline = operation<ExchangeContext> {
    startIf { status == ContextStatus.INIT }
    execute { status = ContextStatus.RUNNING }
}
