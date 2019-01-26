package com.github.assertion.core.junit

import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import org.junit.platform.launcher.listeners.TestExecutionSummary
import java.io.PrintWriter
import java.io.StringWriter

class Junit5Runner(private vararg val classes: Class<*>, val out: PrintWriter = PrintWriter(System.out)) {

    private val success = 0

    private val failed = 1

    private val listener = SummaryGeneratingListener()

    fun run(): Int {
        val request = LauncherDiscoveryRequestBuilder
            .request()
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
Test name ${it.testIdentifier.displayName}
${getStacktraceAsString(it.exception)}
==========================================""".trimIndent()
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
