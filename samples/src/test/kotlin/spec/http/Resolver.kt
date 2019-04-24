package spec.http

import com.github.assertion.core.context.Context

interface Resolver<R> {

    fun resolveAndExecute(context: Context, execute: (R) -> Unit) : Boolean

}