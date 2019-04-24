package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Spec
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.*
import kotlinx.coroutines.runBlocking

class CardStatusInfoResolver : Resolver<CardStatusRq> {

    override fun resolveAndExecute(context: Context, execute: (CardStatusRq) -> Unit): Boolean {
        val info: CardInfoRs = context.tryGet(CardInfoRs::class) ?: return false
        val rq = CardStatusRq(info.id, "active")
        context[CardStatusRq::class] = rq
        execute(rq)
        return true
    }
}

class CardStatusSpec(
    private val resolvers: List<Resolver<CardStatusRq>> = listOf(
        CardStatusInfoResolver()
    )
) : Spec {

    override fun spec(): Specification {
        return specification("getCardInfo") {
            action { context ->
                resolvers.first {
                    //todo need carried function
                    it.resolveAndExecute(context) { rq ->
                        val executeService =
                            ExecuteService("http://localhost:8080/card/status/")
                        runBlocking {
                            val response = executeService.post<CardStatusRs>(rq)
                            println("card status response $response")
                            context[CardStatusRs::class] = response
                        }
                    }
                }
            }
        }
    }
}