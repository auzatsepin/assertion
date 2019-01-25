package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

interface Action {
    fun perform(context: Context)
}

@DslMarker
annotation class SpecDsl

@SpecDsl
fun specification(name: String, setup: SpecificationBuilder.() -> Unit): Specification {
    val builder = SpecificationBuilder(name)
    builder.setup()
    return builder.build()
}

@SpecDsl
class Specification(val name: String, private val actions: List<Action>) {

    private lateinit var context: Context

    operator fun invoke() {
        if (!this::context.isInitialized) throw RuntimeException("Context not initialized")
        actions.forEach {
            it.perform(context)
        }
    }

    infix fun with(context: Context): Specification {
        if (this::context.isInitialized) throw RuntimeException("Context already initialized")
        this.context = context
        return this
    }
}

@SpecDsl
class SpecificationBuilder(private val name: String) {

    private val actions = mutableListOf<Action>()

    fun action(action: (Context) -> Unit) {
        actions += object : Action {
            override fun perform(context: Context) {
                action(context)
            }
        }
    }

    fun action(action: Action) {
        actions += action
    }

    fun verify(verifier: (Context) -> Unit) {
        actions += object : Action {
            override fun perform(context: Context) {
                verifier(context)
            }
        }
    }

    fun verify(verifier: Action) {
        actions += verifier
    }

    fun build(): Specification {
        return Specification(name, actions)
    }

    @Suppress("UNUSED_PARAMETER")
    @Deprecated(level = DeprecationLevel.ERROR, message = "specification can't be nested.")
    fun specification(setup: SpecificationBuilder.() -> Unit = {}) {
    }

}

