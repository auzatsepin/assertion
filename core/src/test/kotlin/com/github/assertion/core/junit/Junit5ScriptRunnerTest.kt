package com.github.assertion.core.junit

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class Junit5ScriptRunnerTest {

    @Test
    fun `should execute tests from scripts`() {
        val specs = Junit5ScriptRunnerTest::class.java.getResourceAsStream("/spec/")
        if (specs == null) {
            fail<Void>("Not one script not found")
        }
        val runner = Junit5ScriptRunner()
        runner.run()
    }

}