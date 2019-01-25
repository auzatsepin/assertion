package com.github.assertion.runner

import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import java.io.PrintWriter
import java.io.StringWriter

class Junit5Runner(private vararg val classes: Class<*>) {

    private val listener = SummaryGeneratingListener()

    fun run() {
        val request = LauncherDiscoveryRequestBuilder
            .request()
            .selectors(classes.map {
                selectClass(it)
            })
            .build()
        val launcher = LauncherFactory.create()
        launcher.registerTestExecutionListeners(listener)
        launcher.execute(request)
        listener.summary.printTo(PrintWriter(System.out))
        println("Failed test details:")
        listener.summary.failures.forEach {
            println(
                """==========================================
Test name ${it.testIdentifier.displayName}
${getStacktraceAsString(it.exception)}
==========================================""".trimIndent()
            )
        }
    }

    private fun getStacktraceAsString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw, true)
        throwable.printStackTrace(pw)
        return sw.buffer.toString()
    }

}
