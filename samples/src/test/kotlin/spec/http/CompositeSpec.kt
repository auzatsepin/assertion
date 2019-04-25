package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Spec
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardInfoRs
import com.github.assertion.samples.http.CardIssueRq
import com.github.assertion.samples.http.CardIssueRs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.*

class CompositeSpec : AbstractTest() {

    companion object : Spec {

        override fun spec(): Specification {
            return specification {
                include(CardIssueSpec().spec())
                include(CardInfoSpec().spec())
                verify {
                    val issueRs: CardIssueRs = it[CardIssueRs::class]
                    assertEquals(0, issueRs.rc)
                    assertNotNull(issueRs.id)
                    assertNotNull(issueRs.pan)
                    assertNotNull(issueRs.psn)
                    val infoRs: CardInfoRs = it[CardInfoRs::class]
                    assertEquals(0, infoRs.rc)
                    assertNotNull(infoRs.id)
                    assertNotNull(infoRs.card)
                }
            }
        }
    }

    @Test
    fun `should issue card and then get card info`() {
        spec() invoke Context()
            .with(CardIssueRq::class to CardIssueRq(UUID.randomUUID().toString()))
    }

}