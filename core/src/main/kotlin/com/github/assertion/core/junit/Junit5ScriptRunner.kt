package com.github.assertion.core.junit

import com.github.assertion.core.context.Context
import com.github.assertion.core.dsl.Specification
import com.github.assertion.core.loader.ScriptLoader
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import org.junit.platform.launcher.listeners.TestExecutionSummary
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class Junit5ScriptRunner(private vararg val classes: Class<*>, private val out: PrintWriter = PrintWriter(System.out)) {

    private val success = 0

    private val failed = 1

    private val listener = SummaryGeneratingListener()

    fun run(): Int {
        val request = LauncherDiscoveryRequestBuilder
            .request()
            .selectors()
            .selectors(classes.map {
                selectClass(it)
            })
            .build()
        val launcher = LauncherFactory.create()
        launcher.registerTestExecutionListeners(listener)
        launcher.execute(request)
        listener.summary.printTo(out)
        println("Failed test details:")
        listener.summary.failures.forEach {
            println(
                """==========================================
                    |Test name ${it.testIdentifier.displayName}
                    |${getStacktraceAsString(it.exception)}
                    |=========================================="""
                    .trimMargin("|")
            )
        }
        return computeExitCode(listener.summary)
    }

    private fun computeExitCode(summary: TestExecutionSummary): Int {
        return if (summary.totalFailureCount != 0L) {
            failed
        } else {
            success
        }
    }

    private fun getStacktraceAsString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw, true)
        throwable.printStackTrace(pw)
        return sw.buffer.toString()
    }

}

class JunitTestFactory {

    @TestFactory
    fun test(): Collection<DynamicTest> {
        val path = System.getProperty("scripts")
        val scripts =
            Files.walk(Paths.get(path) ?: throw IllegalArgumentException("Not found scripts at path $path"))
                .collect(Collectors.toList())
        val loader = ScriptLoader()
        return scripts
            .filter { !Files.isDirectory(it) }
            .map { it }
            .associate { it to loader.load<Specification>(Files.newInputStream(it)) }
            .map { DynamicTest.dynamicTest("${it.key}:${it.value.name}") { it.value(Context()) } }
    }

}
