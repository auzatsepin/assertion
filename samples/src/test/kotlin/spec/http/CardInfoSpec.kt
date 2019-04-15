package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardInfoRq
import com.github.assertion.samples.http.CardInfoRs
import com.github.assertion.samples.http.CardIssueRs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import spec.ITest
import java.util.*

class CardInfoSpec {

    companion object : ITest {
        override fun spec(): Specification {
            return specification("getCardInfo") {
                action { context ->
                    runBlocking {
                        val rq = resolve(context)
                        val executeService =
                            ExecuteService("http://localhost:8080/card/info/?id=${rq.id}&pan=${rq.pan}&psn=${rq.psn}")
                        val response = executeService.get<CardInfoRs>()
                        println("card info response $response")
                        context[CardInfoRs::class] = response
                    }
                }
            }
        }

        private fun resolve(context: Context): CardInfoRq {
            val issue: CardIssueRs = context[CardIssueRs::class]
            val rq = CardInfoRq(issue.id, issue.pan, issue.psn)
            context[CardInfoRq::class] = rq
            return rq
        }
    }

    @Test
    fun cardInfo() {
        val context = spec().invoke(
            Context().with(CardInfoRq::class to CardInfoRq(UUID.randomUUID().toString(), "1", 1))
        )
        val rs: CardInfoRs = context[CardInfoRs::class]
        assertNotNull(rs)
        assertEquals(96, rs.rc)
    }

}