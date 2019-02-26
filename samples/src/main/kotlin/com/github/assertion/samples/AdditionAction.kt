package com.github.assertion.samples

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Action
import com.github.assertion.core.junit.Junit5ScriptRunner
import com.github.assertion.core.junit.JunitTestFactory
import org.junit.jupiter.api.Assertions.assertEquals

class AdditionAction(private val inCtxName: String, private val outCtxName: String) : Action("aa") {

    override fun perform(context: Context) {
        val pair: Pair<Int, Int> = context[inCtxName]
        context[outCtxName] = pair.first + pair.second
    }
}

class AdditionVerifier(private val inCtxName: String, private val expected: Int) : Action("av") {

    override fun perform(context: Context) {
        val result: Int = context[inCtxName]
        assertEquals(expected, result, "Addition verifier")
    }
}

fun main() {
    val spec = AdditionAction::javaClass.javaClass.getResource("/spec")
    System.setProperty("scripts", spec.toURI().path)
    val junit5Runner = Junit5ScriptRunner(JunitTestFactory::class.java)
    val result = junit5Runner.run()
    System.exit(result)
}
