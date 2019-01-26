package com.github.assertion.samples

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Action
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.junit.Junit5Runner
import com.github.assertion.core.loader.ScriptLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class AdditionAction(private val inCtxName: String, private val outCtxName: String) : Action {

    override fun perform(context: Context) {
        val pair: Pair<Int, Int> = context[inCtxName]
        context[outCtxName] = pair.first + pair.second
    }
}

class AdditionVerifier(private val inCtxName: String, private val expected: Int) : Action {

    override fun perform(context: Context) {
        val result: Int = context[inCtxName]
        assertEquals(expected, result)
    }
}

class Test {

    @TestFactory
    fun test(): Collection<DynamicTest> {
        val path = System.getProperty("scripts")
        val scripts =
            Files.walk(Paths.get(path) ?: throw IllegalArgumentException("Not found scripts at path $path"))
                .collect(Collectors.toList())
        val loader = ScriptLoader()
        return loader.loadAll<Specification>(scripts
            .filter { !Files.isDirectory(it) }
            .map { it }
            .map { Files.newInputStream(it) })
            .map { DynamicTest.dynamicTest(it.name) { it() } }
    }

}

fun main(args: Array<String>) {
    val path = args[0]
    System.setProperty("scripts", path)
    val junit5Runner = Junit5Runner(Test::class.java)
    val result = junit5Runner.run()
    System.exit(result)
}
