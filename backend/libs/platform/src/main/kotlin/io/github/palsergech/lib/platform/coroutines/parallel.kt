package io.github.palsergech.lib.platform.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> Iterable<T>.parallelMap(maxParallelRequests: Int = 10, transform: suspend (T) -> R): List<R> {
    val items = this
    return coroutineScope {
        items
            .chunked(maxParallelRequests)
            .flatMap { chunk ->
                chunk
                    .map {
                        async(Dispatchers.IO) {
                            transform(it)
                        }
                    }
                    .awaitAll()
            }
    }
}
