package spec.http

import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardIssueRq
import com.github.assertion.samples.http.CardIssueRs
import kotlinx.coroutines.runBlocking
import spec.ITest

class CardIssueSpec {

    companion object : ITest {

        override fun spec(): Specification {
            return specification("getCardInfo") {
                action { context ->
                    runBlocking {
                        val issueRq: CardIssueRq = context[CardIssueRq::class]
                        val executeService =
                            ExecuteService("http://localhost:8080/card/issue/")
                        val response = executeService.put<CardIssueRs>(issueRq)
                        println("card issue response $response")
                        context[CardIssueRs::class] = response
                    }
                }
            }
        }
    }
}