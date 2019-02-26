import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.AdditionAction
import com.github.assertion.samples.AdditionVerifier
import org.junit.jupiter.api.Assertions.assertEquals

val inCtxName = "in"
val outCtxName = "out"

val context = Context().apply {
    this[inCtxName] = Pair(2, 3)
    set("alternative", Pair(2, 3))
}

specification("multiple fail") {

    specification("add failed", context) {
        action(AdditionAction(inCtxName, outCtxName))
        verify(AdditionVerifier(outCtxName, 15))
    }

    specification("asd", context) {
        action("asd_5_10") {
            assertEquals(5, 10)
        }
    }
}