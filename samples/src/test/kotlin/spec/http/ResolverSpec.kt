package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardInfoRs
import com.github.assertion.samples.http.CardIssueRq
import com.github.assertion.samples.http.CardIssueRs
import com.github.assertion.samples.http.CardStatusRs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.*

class ResolverSpec : AbstractTest() {

    @Test
    fun `should change status and get info for exist card`() {
        val issueContext = Context(CardIssueRq::class to CardIssueRq(UUID.randomUUID().toString()))
        specification("issue card") {
            include(CardIssueSpec.spec())
            verify { context ->
                val rs: CardIssueRs = context[CardIssueRs::class]
                Assertions.assertEquals(0, rs.rc)
            }
        } invoke (issueContext)
        val issueRsContext = Context(CardIssueRs::class to issueContext[CardIssueRs::class])
        specification("get card info") {
            include(CardInfoSpec().spec())
            verify { context ->
                val rs: CardInfoRs = context[CardInfoRs::class]
                Assertions.assertEquals(0, rs.rc)
            }
        } invoke issueRsContext
        specification("change card status") {
            include(CardStatusSpec().spec())
            verify {context ->
                val rs : CardStatusRs = context[CardStatusRs::class]
                assertNotNull(rs)
            }
        } invoke issueRsContext
        val statusContext = Context(CardStatusRs::class to issueRsContext[CardStatusRs::class])
        specification("get card info with status") {
            include(CardInfoSpec().spec())
            verify { context ->
                val rs: CardInfoRs = context[CardInfoRs::class]
                Assertions.assertEquals(0, rs.rc)
            }
        } invoke statusContext
    }
}