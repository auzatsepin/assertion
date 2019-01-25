package com.github.assertion.runner.loader

import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

internal class ScriptLoaderTest {

    @Test
    fun `should load script engine`() {
        ScriptLoader().engine.factory.apply {
            assertEquals("kotlin", languageName)
            assertEquals("kts", extensions.single())
        }
    }

    @Test
    fun `should evaluate kotlin script from string with ScriptEngine`() {
        with(ScriptLoader().engine as KotlinJsr223JvmLocalScriptEngine) {
            val emptyResult = eval("val x = 3")
            assertNull(emptyResult)
            val ten = eval("x + 7")
            assertEquals(10, ten)
        }
    }

    @Test
    fun `should evaluate kotlin script from with ScriptEngine by loader`() {
        ScriptLoader().apply {
            val result = eval {
                val script = "2 + 2"
                engine.eval(script) as Int
            }
            assertNotNull(result)
            assertEquals(4, result)
        }
    }

    @Test
    fun `should load and evaluate script from string`() {
        val script = "3 + 3"
        val result: Int = ScriptLoader().loadAndEval(script)
        assertNotNull(result)
        assertEquals(6, result)
    }

    @Test
    fun `should load and evaluate multiple scripts from InputStreams`() {
        val path = "/spec/success/"
        val specs = ScriptLoaderTest::class.java.getResource(path)
        if (specs == null) {
            fail<Void>("Not found spec directory")
        }
        val walk = Files.walk(Paths.get(specs.toURI()))
        if (walk == null) {
            fail<Void>("Not found scripts in spec directory")
        }
        val scripts = walk.filter { !Files.isDirectory(it) }
            .map { ScriptLoaderTest::class.java.getResourceAsStream("$path${it.fileName}") }
            .collect(Collectors.toList())
        ScriptLoader().loadAll<Unit>(scripts)
    }

}