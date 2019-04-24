package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Spec
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardInfoRq
import com.github.assertion.samples.http.CardInfoRs
import com.github.assertion.samples.http.CardIssueRs
import com.github.assertion.samples.http.CardStatusRs
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class CardInfoRqResolver : Resolver<CardInfoRq> {
    override fun resolveAndExecute(context: Context, execute: (CardInfoRq) -> Unit): Boolean {
        val issue: CardIssueRs = context.tryGet(CardIssueRs::class) ?: return false
        val rq = CardInfoRq(issue.id, issue.pan, issue.psn)
        context[CardInfoRq::class] = rq
        execute(rq)
        return true
    }
}

class CardStatusResolver : Resolver<CardInfoRq> {
    override fun resolveAndExecute(context: Context, execute: (CardInfoRq) -> Unit): Boolean {
        val issue: CardStatusRs = context.tryGet(CardStatusRs::class) ?: return false
        if (issue.card == null) throw IllegalArgumentException("Card at status response is absent")
        val rq = CardInfoRq(issue.id, issue.card!!.pan, issue.card!!.psn)
        context[CardInfoRq::class] = rq
        execute(rq)
        return true
    }
}

class CardInfoSpec(
    private val inCtxVarName: Any = CardInfoRq::class,
    private val outCtxVarName: Any = CardInfoRs::class,
    private val resolvers: List<Resolver<CardInfoRq>> = listOf(
        CardInfoRqResolver(),
        CardStatusResolver()
    )
) : Spec {

    override fun spec(): Specification {
        return specification("getCardInfo") {
            action { context ->
                resolvers.first {
                    it.resolveAndExecute(context) { rq ->
                        val executeService =
                            ExecuteService("http://localhost:8080/card/info/?id=${rq.id}&pan=${rq.pan}&psn=${rq.psn}")
                        runBlocking {
                            val response = executeService.get<CardInfoRs>()
                            println("card info response $response")
                            context[outCtxVarName] = response
                        }
                    }
                }
            }
        }
    }
}
