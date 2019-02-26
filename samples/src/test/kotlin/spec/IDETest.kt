package spec

import com.github.assertion.core.dsl.specification
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

object Test {

    @Test
    fun test() {
        val context = specification("name") {

            specification("f") {
                action("f_t_f") {
                    assertEquals(true, false)
                }
            }

            specification("s") {
                action("s_1_5") {
                    assertEquals(1, 5)
                }
            }
        }.invoke()
        assertEquals(context.problems.size(), 2)
        context.problems.show()
    }

    @Test
    fun regularTest() {
        val ex = assertThrows(
            IllegalArgumentException::class.java
        ) { throw IllegalArgumentException("42") }
        assertNotNull(ex)
    }

}

