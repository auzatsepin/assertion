package spec.http

import com.github.assertion.samples.http.HttpServer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class AbstractTest {

    private lateinit var server: HttpServer

    @BeforeAll
    fun init() {
        println("starting http server")
        server = HttpServer()
        server.start()
    }

    @AfterAll
    fun tearDown() {
        println("stopping http server")
        runBlocking {
            ExecuteService("http://localhost:8080/shutdown").get<Any>()
        }
    }

}