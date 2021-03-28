package ru.otus.cor

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PipelineTest : FunSpec() {

    init {
        test("simple pipeline") {
            val givenOperation = operation<TestContext> {
                execute { a += "c" }
            }
            val givenPipeline = pipeline<TestContext> {
                execute { a = "a" }
                execute(givenOperation)
                operation {
                    startIf {
                        println("Check a: $a")
                        a.isNotEmpty()
                    }
                    execute { a += "b" }
                }
            }
            val givenContext = TestContext()
            println("Starting test")
            givenPipeline.execute(givenContext)
            givenContext.a shouldBe "acb"
            println("Test Done")
            println("Finish test")
        }
        test("nested pipeline") {
            val givenOperation = operation<TestContext> {
                execute { a += "c" }
            }
            val givenPipeline = pipeline<TestContext> {
                execute { a = "a" }
                execute(givenOperation)
                pipeline {
                    startIf {
                        println("Check a: $a")
                        a.isNotEmpty()
                    }
                    execute { a += "b" }
                }
            }
            val givenContext = TestContext()

            println("Starting test")
            givenPipeline.execute(givenContext)
            givenContext.a shouldBe "acb"
            println("Test Done")
            println("Finish test")
        }
        test("validation test") {
            val givenContext = TestContext()
            val givenValidation = validation<TestContext> {
                validate { a.isNotBlank() }
            }
            shouldThrowExactly<ValidationException> {
                givenValidation.execute(givenContext)
            }

            val anotherValidation = validation<TestContext> {
                validate { a.isNotBlank() }
                withMessage("Field is blank")
            }
            shouldThrowMessage("Field is blank") {
                anotherValidation.execute(givenContext)
            }

            val oneMoreValidation = validation<TestContext> {
                validate { a.isNotBlank() }
                withMessage("Field is blank")
                onFail { println("Validation do nothing") }
            }
            val givenOperation = operation<TestContext> {
                execute { a += "c" }
            }
            val givenPipeline = pipeline<TestContext> {
                validate(oneMoreValidation)
                execute { a = "a" }
                execute(givenOperation)
                validation {
                    validate { a.length == 3 }
                }
                handleValidationFail {
                    println(it.message)
                }
                execute { a += "b" }
            }
            givenPipeline.execute(givenContext)
            givenContext.a shouldBe "ac"
        }
    }
}

private data class TestContext(
    var a: String = ""
)
