package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

interface Action {
    fun perform(context: Context)
}

@DslMarker
annotation class SpecDsl

@SpecDsl
fun specification(
    setup: SpecificationBuilder.() -> Unit
): Specification {
    val builder = SpecificationBuilder()
    builder.setup()
    return builder.build()
}

@SpecDsl
class Specification(
    internal val actions: List<Action>
) : Action {

    infix operator fun invoke(context: Context): Context {
        actions.forEach {
            try {
                it.perform(context)
            } catch (e: Throwable) {
                throw e
            }
        }
        return context
    }

    override fun perform(context: Context) {
        this(context)
    }
}

@SpecDsl
class SpecificationBuilder(
    private val actions: MutableList<Action> = mutableListOf()
) {


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
        action(verifier)
    }

    fun build(): Specification {
        return Specification(actions)
    }

    fun include(spec: Specification) {
        actions += spec.actions
    }

    fun specification(name: String, setup: SpecificationBuilder.() -> Unit) {
        val builder = SpecificationBuilder()
        builder.setup()
        actions += builder.build()
    }

}

