package spec.simple

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Spec
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CustomSumTest {

    companion object : Spec {
        override fun spec(): Specification {
            return specification("sum") {
                action { context ->
                    val p: Int = context["sp"]
                    context["sr"] = p + p
                }
            }
        }
    }

    @Test
    fun sumTest() {
        val context = spec().invoke(Context().with("sp" to 5))
        assertEquals(10, context["sr"])
    }


}

class CustomMulTest : Spec {

    override fun spec(): Specification {
        return specification("mul") {
            action { context ->
                val p: Int = context["sr"]
                context["mr"] = p * p
            }
        }
    }

    @Test
    fun mulTest() {
        val invoke = spec().invoke(Context().with("sr" to 5))
        assertEquals(25, invoke["mr"])
    }

}

class SpecInclude {

    @Test
    fun `should sum then multiply`() {
        val context = specification("sum then multiply") {
            include(CustomSumTest.spec())
            include(CustomMulTest().spec())
        }.invoke(Context().with("sp" to 5))
        assertEquals(100, context["mr"])
    }

}