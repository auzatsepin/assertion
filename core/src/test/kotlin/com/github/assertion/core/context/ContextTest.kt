package com.github.assertion.core.context

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ContextTest {

    private lateinit var context: Context

    @BeforeEach
    fun init() {
        context = Context()
    }

    @Test
    fun `should get parameter with get method`() {
        context["a"] = "b"
        val value = context.get<String>("a")
        assertEquals("b", value)
    }

    @Test
    fun `should get parameter with get operator`() {
        context["a"] = "b"
        val value: String = context["a"]
        assertEquals("b", value)
    }

    @Test
    fun `should throw exception if param name not exist`() {
        val assertThrows = assertThrows(ParamNotFoundException::class.java) {
            context["a"]
        }
        assertEquals("Param [a] not found", assertThrows.message)
    }

    @Test
    fun `should throw exception if trying to get param with incorrect type`() {
        context["a"] = 1
        val assertThrows = assertThrows(IncorrectParamTypeException::class.java) {
            @Suppress("UNUSED_VARIABLE") val value: String = context["a"]
        }
        assertEquals(
            "Found param with name -> a but type is incorrect: class java.lang.String != class java.lang.Integer",
            assertThrows.message
        )
    }

    @Test
    fun `should throw exception if trying to get param without set type`() {
        context["a"] = 1
        val assertThrows = assertThrows(IncorrectParamTypeException::class.java) {
            context["a"]
        }
        assertEquals(
            "Found param with name -> a but type is incorrect: class kotlin.Unit != class java.lang.Integer",
            assertThrows.message
        )
    }

}