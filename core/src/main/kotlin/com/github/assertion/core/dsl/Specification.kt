package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

interface Action {
    fun perform(context: Context)
}

@DslMarker
annotation class SpecDsl

@SpecDsl
fun specification(name: String, context: Context = Context(), setup: SpecificationBuilder.() -> Unit): Specification {
    val builder = SpecificationBuilder(name, context)
    builder.setup()
    return builder.build()
}

@SpecDsl
class Specification(val name: String, private val context: Context, private val actions: List<Action>) : Action {

    operator fun invoke(): Context {
        actions.forEach {
            try {
                it.perform(context)
            } catch (e: Throwable) {
                context.problems[it] = e
            }
        }
        return context
    }

    override fun perform(context: Context) {
        this()
    }
}

@SpecDsl
class SpecificationBuilder(private val name: String, private val context: Context) {

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
        return Specification(name, context, actions)
    }

    @SpecDsl
    fun specification(name: String, setup: SpecificationBuilder.() -> Unit) {
        val builder = SpecificationBuilder(name, context)
        builder.setup()
        actions += builder.build()
    }

}

