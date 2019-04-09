package com.github.assertion.core.context

import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.ConcurrentHashMap

class Context {

    private val params = ConcurrentHashMap<Any, Any>()

    fun size(): Int = params.size

    val problems = Problems()

    @PublishedApi
    internal val paramsInternal: MutableMap<Any, Any>
        get() = params

    operator fun set(name: Any, value: Any) {
        params[name] = value
    }

    inline operator fun <reified T> get(name: Any): T {
        val value = paramsInternal[name] ?: throw ParamNotFoundException(name)
        if (value is T) {
            return value
        } else {
            throw IncorrectParamTypeException(name, T::class.java, value::class.java)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String, clazz: Class<T>): T {
        val value = params[name] ?: throw ParamNotFoundException(name)
        if (value.javaClass == clazz) {
            return value as T
        } else {
            throw IncorrectParamTypeException(name, clazz, value::class.java)
        }

    }

    override fun toString(): String {
        return "Context(params=$params, problems=$problems)"
    }

    fun with(vararg pairs: Pair<Any, Any>): Context {
        pairs.forEach {
            params[it.first] = it.second
        }
        return this
    }

}

class Problems {
    private val problems = mutableMapOf<String, Throwable>()

    fun add(name: String, throwable: Throwable) {
        problems[name] = throwable
    }

    fun show() {
        problems.forEach {entry ->
            println(problemToString(entry.key, entry.value))
        }
    }

    fun size()  = problems.size

    private fun problemToString(name : String, throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw, true)
        pw.println("test ===> $name")
        throwable.printStackTrace(pw)
        return sw.buffer.toString()
    }
}

class IncorrectParamTypeException(name: Any, inputType: Class<*>, contextType: Class<*>) :
    UnsupportedOperationException() {
    override val message: String? = "Found param with name -> $name but type is incorrect: $inputType != $contextType"
}

class ParamNotFoundException(name: Any) : java.lang.UnsupportedOperationException() {
    override val message: String? = "Param [$name] not found"
}
