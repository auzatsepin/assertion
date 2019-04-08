package spec

import com.github.assertion.samples.http.CardInfoRs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import spec.http.ExecuteService

class Test {

    @Test
    fun tr() {
        runBlocking { sendRq() }
    }

    suspend fun sendRq() {
        val executeService = ExecuteService("http://localhost:8080/card/info/?id=qweqweqwe&pan=2495321123323738&psn=1")
        val get = executeService.get<CardInfoRs>()
        println(get)
    }

}