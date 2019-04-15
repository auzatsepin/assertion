package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardInfoRs
import com.github.assertion.samples.http.CardIssueRq
import com.github.assertion.samples.http.CardIssueRs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class CompositeSpec {

    @Test
    fun `should issue card and then get card info`() {
        specification("issue card and then get info") {
            //execute issue card spec
            include(CardIssueSpec.spec())
            //verify issue card result
            verify { context ->
                println("verify 1")
                val rs: CardIssueRs = context[CardIssueRs::class]
                Assertions.assertEquals(0, rs.rc)
                Assertions.assertNotNull(rs.id)
                Assertions.assertNotNull(rs.pan)
                Assertions.assertNotNull(rs.psn)
                println(rs)
            }
            //get info by issued card
            include(CardInfoSpec.spec())
            //get info by issued card
            verify { context ->
                println("verify 2")
                val rs: CardInfoRs = context[CardInfoRs::class]
                Assertions.assertEquals(0, rs.rc)
                Assertions.assertNotNull(rs.id)
                Assertions.assertNotNull(rs.card)
                println(rs)
            }
        } invoke Context().with(CardIssueRq::class to CardIssueRq(UUID.randomUUID().toString()))
    }

}