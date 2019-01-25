package com.github.assertion.core.junit

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class Junit5RunnerTest {

    @Test
    fun `should execute tests from scripts`() {
        val specs = Junit5RunnerTest::class.java.getResourceAsStream("/spec/")
        if (specs == null) {
            fail<Void>("Not one script not found")
        }
        val runner = Junit5Runner()
        runner.run()
    }

}