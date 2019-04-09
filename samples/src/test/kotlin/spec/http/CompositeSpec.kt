package spec.http

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.specification
import com.github.assertion.samples.http.CardIssueRq
import com.github.assertion.samples.http.CardIssueRs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

class CompositeSpec {

    @Disabled
    @Test
    fun `should issue card and then get card info`() {
        val spec = specification("issue card and then get info") {
            include(CardIssueSpec.spec())
            action { context ->
                Assertions.assertNotNull(context[CardIssueRs::class])
                include(CardInfoSpec.spec())
            }
/*            action { context ->
                Assertions.assertNotNull(context[CardIssueRs::class])
                include(CardInfoSpec.spec())
                action {
                    Assertions.assertNotNull(context[CardInfoRs::class])
                }
            }*/
        }
        val context = Context().with(CardIssueRq::class to CardIssueRq(UUID.randomUUID().toString()))
        spec.invoke(context)
    }

}