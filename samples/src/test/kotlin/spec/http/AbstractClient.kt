package spec.http

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import java.text.DateFormat

class ExecuteService(val url: String) {

    val httpClient = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                disableDefaultTyping()
                findAndRegisterModules()
            }
        }
    }

    suspend inline fun <reified T> get(): T {
        val r: T
        try {
            r = httpClient.get(url)
        } catch (e: Throwable) {
            httpClient.close()
            throw e
        }
        return r
    }
}
