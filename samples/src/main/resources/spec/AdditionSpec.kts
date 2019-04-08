import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.AdditionAction
import com.github.assertion.samples.AdditionVerifier

val inCtxName = "in"
val outCtxName = "out"

specification("add success") {
    val additionAction = AdditionAction(inCtxName, outCtxName)
    val additionVerifier = AdditionVerifier(outCtxName, 5)
    action(additionAction)
    verify(additionVerifier)
}