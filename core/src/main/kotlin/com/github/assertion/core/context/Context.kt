package com.github.assertion.core.context

import com.github.assertion.core.dsl.Action

class Context {

    private val params = mutableMapOf<String, Any>()

    fun size() : Int = params.size

    val problems = mutableMapOf<Action, Throwable>()

    @PublishedApi
    internal val paramsInternal: MutableMap<String, Any>
        get() = params

    operator fun set(name: String, value: Any) {
        params[name] = value
    }

    inline operator fun <reified T> get(name: String): T {
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


}

class IncorrectParamTypeException(name: String, inputType: Class<*>, contextType: Class<*>) :
    UnsupportedOperationException() {
    override val message: String? = "Found param with name -> $name but type is incorrect: $inputType != $contextType"
}

class ParamNotFoundException(name: String) : java.lang.UnsupportedOperationException() {
    override val message: String? = "Param [$name] not found"
}
