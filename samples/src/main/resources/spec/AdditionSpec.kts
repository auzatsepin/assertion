import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.AdditionAction
import com.github.assertion.samples.AdditionVerifier

val inCtxName = "in"
val outCtxName = "out"

//todo wrap to class with single method annotated with junit @Test
specification {
    val additionAction = AdditionAction(inCtxName, outCtxName)
    val additionVerifier = AdditionVerifier(outCtxName, 5)
    action(additionAction)
    verify(additionVerifier)
} with Context().apply {
    this[inCtxName] = Pair(2, 3)
    set("alternative", Pair(2, 3)) //todo can be infix for example -> name "a" value "b"
}