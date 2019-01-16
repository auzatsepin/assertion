package com.github.assertion.samples

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Action
import org.junit.jupiter.api.Assertions.assertEquals

class AdditionAction(private val inCtxName: String, private val outCtxName: String) : Action {

    override fun perform(context: Context) {
        val pair: Pair<Int, Int> = context[inCtxName]
        context[outCtxName] = pair.first + pair.second
    }
}

class AdditionVerifier(private val inCtxName: String, private val expected: Int) : Action {

    override fun perform(context: Context) {
        val result: Int = context[inCtxName]
        assertEquals(expected, result)
    }
}
