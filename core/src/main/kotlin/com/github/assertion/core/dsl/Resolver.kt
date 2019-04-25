package com.github.assertion.core.dsl

import com.github.assertion.core.context.Context

interface Resolver<R> {

    fun resolveAndExecute(context: Context, execute: (R) -> Unit) : Boolean

}