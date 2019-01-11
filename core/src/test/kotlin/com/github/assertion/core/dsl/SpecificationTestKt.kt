package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SpecificationTest {

    @Test
    fun `should execute spec with anonymous action`() {
        specification {
            action(object : Action {
                override fun perform(context: Context) {
                    context["third"] = 3
                }
            })
            verify(object : Action {
                override fun perform(context: Context) {
                    assertEquals(3, context["third"])
                }
            })
            verify(object : Action {
                override fun perform(context: Context) {
                    assertEquals(3, context["third"])
                }
            })
            action(object : Action {
                override fun perform(context: Context) {
                    context["fourth"] = 4
                }
            })
            verify(object : Action {
                override fun perform(context: Context) {
                    assertEquals(4, context["fourth"])
                }
            })
        }()
    }

    @Test
    fun `should execute spec with functional action`() {
        specification {
            action { context ->
                context["third"] = 3
            }
            verify { context ->
                assertEquals(3, context["third"])
            }
        }()
    }

    @Test
    fun `should execute already defined action and verifier`() {
        val inCtxName = "in"
        val outCtxName = "out"
        val context = Context()
        context[inCtxName] = MultiplierIn(2, 2)
        specification(context) {
            action(MultiplyAction(inCtxName, outCtxName))
            verify(MultiplyVerifier(outCtxName, 4))
        }()
    }

    @Test
    fun `should execute already defined action and custom verifier`() {
        val inCtxName = "in"
        val outCtxName = "out"
        val context = Context()
        context[inCtxName] = MultiplierIn(2, 2)
        specification(context) {
            action(MultiplyAction(inCtxName, outCtxName))
            verify { context ->
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
        specification {
            action(MultiplyAction(inCtxName, outCtxName))
            verify(MultiplyVerifier(outCtxName, 4))
        } with context
    }

}

data class MultiplierIn(val first: Int, val second: Int)

data class MultiplierOut(val result: Int)

class MultiplyAction(private val inCtxName: String, private val outCtxName: String) : Action {

    override fun perform(context: Context) {
        val multiplierIn: MultiplierIn = context[inCtxName]
        context[outCtxName] = MultiplierOut(multiplierIn.first * multiplierIn.second)
    }

}

class MultiplyVerifier(private val outCtxName: String, private val expected: Int) : Action {

    override fun perform(context: Context) {
        val out: MultiplierOut = context[outCtxName]
        assertEquals(expected, out.result)
    }
}
