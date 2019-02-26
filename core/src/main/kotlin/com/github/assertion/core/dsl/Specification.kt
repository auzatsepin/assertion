package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

abstract class Action(val actionName: String) {
    abstract fun perform(context: Context)
}

@DslMarker
annotation class SpecDsl

@SpecDsl
fun specification(
    name: String,
    context: Context = Context(),
    catchError: Boolean = true,
    setup: SpecificationBuilder.() -> Unit
): Specification {
    val builder = SpecificationBuilder(name, catchError, context)
    builder.setup()
    return builder.build()
}

@SpecDsl
class Specification(
    val name: String,
    private val context: Context,
    private val actions: List<Action>,
    private val catchErrors: Boolean = true
) : Action(name) {

    operator fun invoke(): Context {
        actions.forEach {
            try {
                it.perform(context)
            } catch (e: Throwable) {
                if (catchErrors) {
                    context.problems.add(it.actionName, e)
                } else {
                    throw e
                }
            }
        }
        return context
    }

    override fun perform(context: Context) {
        this()
    }
}

@SpecDsl
class SpecificationBuilder(
    private val name: String,
    private val catchError: Boolean = true,
    private val context: Context
) {

    private val actions = mutableListOf<Action>()

    fun action(name: String, action: (Context) -> Unit) {
        actions += object : Action(name) {
            override fun perform(context: Context) {
                action(context)
            }
        }
    }

    fun action(action: Action) {
        actions += action
    }

    fun verify(name: String, verifier: (Context) -> Unit) {
        actions += object : Action(name) {
            override fun perform(context: Context) {
                verifier(context)
            }
        }
    }

    fun verify(verifier: Action) {
        actions += verifier
    }

    fun build(): Specification {
        return Specification(name, context, actions, catchError)
    }

    @SpecDsl
    fun specification(name: String, catchErrors: Boolean = true, setup: SpecificationBuilder.() -> Unit) {
        val builder = SpecificationBuilder(name, catchErrors, context)
        builder.setup()
        actions += builder.build()
    }

}

