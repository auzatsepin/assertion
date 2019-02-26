package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

internal class SpecificationTest {

    @Test
    fun `should execute spec with anonymous action`() {
        specification("anon") {
            action(object : Action("third_set") {
                override fun perform(context: Context) {
                    context["third"] = 3
                }
            })
            verify(object : Action("third_get") {
                override fun perform(context: Context) {
                    assertEquals(3, context["third"])
                }
            })
            verify(object : Action("third_get") {
                override fun perform(context: Context) {
                    assertEquals(3, context["third"])
                }
            })
            action(object : Action("fourth_set") {
                override fun perform(context: Context) {
                    context["fourth"] = 4
                }
            })
            verify(object : Action("fourth_get") {
                override fun perform(context: Context) {
                    assertEquals(4, context["fourth"])
                }
            })
        }
    }

    @Test
    fun `should execute spec with functional action`() {
        specification("fact") {
            action("a_fact_third") { context ->
                context["third"] = 3
            }
            verify("v_fact_third") { context ->
                assertEquals(3, context["third"])
            }
        }
    }

    @Test
    fun `should execute already defined action and verifier`() {
        val inCtxName = "in"
        val outCtxName = "out"
        val context = Context()
        context[inCtxName] = MultiplierIn(2, 2)
        specification("execute") {
            action(MultiplyAction(inCtxName, outCtxName))
            verify(MultiplyVerifier(outCtxName, 4))
        }
    }

    @Test
    fun `should execute already defined action and custom verifier`() {
        val inCtxName = "in"
        val outCtxName = "out"
        val context = Context()
        context[inCtxName] = MultiplierIn(2, 2)
        specification("already defined action") {
            action(MultiplyAction(inCtxName, outCtxName))
            verify("v_4_result") { context ->
                val result: MultiplierOut = context[outCtxName]
                assertEquals(4, result.result)
            }
        }
    }

    @Test
    fun `should execute when set context to specification invoke`() {
        val inCtxName = "in"
        val outCtxName = "out"
        val context = Context()
        context[inCtxName] = MultiplierIn(2, 2)
        specification("with invoke") {
            action(MultiplyAction(inCtxName, outCtxName))
            verify(MultiplyVerifier(outCtxName, 4))
        }
    }

    @Test
    fun `should throw exception if handleErrors is off`() {
        val ex = Assertions.assertThrows(
            AssertionFailedError::class.java
        ) {
            specification("error", catchError = false) {

                action("fail") {
                    assertEquals("25", "50")
                }

            }.invoke()
        }
        assertNotNull(ex)
    }

}

data class MultiplierIn(val first: Int, val second: Int)

data class MultiplierOut(val result: Int)

class MultiplyAction(private val inCtxName: String, private val outCtxName: String) : Action("ma") {

    override fun perform(context: Context) {
        val multiplierIn: MultiplierIn = context[inCtxName]
        context[outCtxName] = MultiplierOut(multiplierIn.first * multiplierIn.second)
    }

}

class MultiplyVerifier(private val outCtxName: String, private val expected: Int) : Action("mv") {

    override fun perform(context: Context) {
        val out: MultiplierOut = context[outCtxName]
        assertEquals(expected, out.result)
    }
}
