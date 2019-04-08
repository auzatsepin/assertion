import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.AdditionAction
import com.github.assertion.samples.AdditionVerifier
import org.junit.jupiter.api.Assertions.assertEquals

val inCtxName = "in"
val outCtxName = "out"

specification("multiple fail") {

    specification("add failed") {
        action(AdditionAction(inCtxName, outCtxName))
        verify(AdditionVerifier(outCtxName, 15))
    }

    specification("asd") {
        action("asd_5_10") {
            assertEquals(5, 10)
        }
    }
}