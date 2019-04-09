package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NestedSpecificationTest {

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `should invoke nested specs`(@MockK firstMock: MockAction, @MockK secondMock: MockAction) {
        val context = Context()
        every {
            firstMock.perform(any())
        } answers {
            context["out"] = 100
        }
        every {
            secondMock.perform(context)
        } answers {
            context["nestedOut"] = 200
        }
        val spec = specification("spec") {
            action(firstMock)
            specification("innerSpec") {
                action(secondMock)
            }
        }
        spec(context)
        verify(exactly = 1) {
            firstMock.perform(
                withArg {
                    assertEquals(100, it["out"])
                }
            )
        }
        verify(exactly = 1) {
            secondMock.perform(
                withArg {
                    assertEquals(200, it["nestedOut"])
                }
            )
        }
        assertEquals(2, context.size())
    }

}

class MockAction : Action {

    override fun perform(context: Context) {
    }
}