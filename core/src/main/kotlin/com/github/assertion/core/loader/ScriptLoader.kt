package com.github.assertion.core.loader

import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ScriptLoader(
    scriptExtension: String = "kts",
    classLoader: ClassLoader? = Thread.currentThread().contextClassLoader
) {

    val engine: ScriptEngine = ScriptEngineManager(classLoader).getEngineByExtension(scriptExtension)
        ?: throw RuntimeException("Can't load kts script engine")

    inline fun <R> eval(evaluation: () -> R?) = try {
        val result = evaluation()
        result ?: Unit
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

    inline fun <reified T> loadAndEval(script: String) = eval { engine.eval(script) }.castOrThrow<T>()

    inline fun <reified T> Any?.castOrThrow() = takeIf { it is T }?.let { it as T }
        ?: throw IllegalArgumentException("Cannot cast $this to expected type ${T::class}")

    inline fun <reified T> load(reader: Reader): T = eval { engine.eval(reader) }.castOrThrow()

    inline fun <reified T> load(inputStream: InputStream): T = load(inputStream.reader())

    inline fun <reified T> loadAll(inputStream: List<InputStream>): List<T> = inputStream.map(::load)

}