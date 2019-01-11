package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

interface Action {
    fun perform(context: Context)
}

@DslMarker
annotation class Spec

@Spec
fun specification(context: Context, setup: SpecificationBuilder.() -> Unit): Specification {
    val builder = SpecificationBuilder(context)
    builder.setup()
    return builder.build()
}

@Spec
fun specification(setup: SpecificationBuilder.() -> Unit): Specification {
    return specification(Context(), setup)
}

@Spec
class Specification(
    private val context: Context,
    private val actions: List<Action>
) {

    operator fun invoke() {
        actions.forEach {
            it.perform(context)
        }
    }

    infix fun with(context: Context) {
        actions.forEach {
            it.perform(context)
        }
    }
}

@Spec
class SpecificationBuilder(private val context: Context) {

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
        return Specification(context, actions)
    }

    @Suppress("UNUSED_PARAMETER")
    @Deprecated(level = DeprecationLevel.ERROR, message = "specification can't be nested.")
    fun specification(setup: SpecificationBuilder.() -> Unit = {}) {
    }

}

