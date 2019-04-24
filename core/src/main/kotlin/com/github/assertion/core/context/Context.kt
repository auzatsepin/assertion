package com.github.assertion.core.context

import java.util.concurrent.ConcurrentHashMap

class Context {

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(vararg pairs: Pair<Any, Any>) {
        pairs.forEach {
            params[it.first] = it.second
        }
    }

    private val params = ConcurrentHashMap<Any, Any>()

    fun size(): Int = params.size

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

    inline fun <reified T> tryGet(name : Any) : T? {
        val param = paramsInternal[name]
        return if (param == null) {
            null
        } else {
            get(name)
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

    fun with(vararg pairs: Pair<Any, Any>): Context {
        pairs.forEach {
            params[it.first] = it.second
        }
        return this
    }

    override fun toString(): String {
        return "Context(params=$params)"
    }

}

class IncorrectParamTypeException(name: Any, inputType: Class<*>, contextType: Class<*>) :
    UnsupportedOperationException() {
    override val message: String? = "Found param with name -> $name but type is incorrect: $inputType != $contextType"
}

class ParamNotFoundException(name: Any) : java.lang.UnsupportedOperationException() {
    override val message: String? = "Param [$name] not found"
}
