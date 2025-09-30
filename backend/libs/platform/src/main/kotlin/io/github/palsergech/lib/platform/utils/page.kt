package io.github.palsergech.lib.platform.utils

import kotlinx.coroutines.flow.*
import kotlin.math.min

data class PageSpec(
    val limit: Int,
    val offset: Int
) {
    init {
        require(limit > 0) { "limit must be > 0 but got $limit" }
        require(offset >= 0) { "offset must be >= 0 but got $offset" }
    }
}

fun <T> getPagesFlow(pageSize: Int, totalLimit: Int? = null, f: suspend (PageSpec) -> List<T>): Flow<T> {
    require(pageSize > 0) { "pageSize must be positive" }
    require(totalLimit == null || totalLimit > 0) {
        "total limit must be positive if provided"
    }
    return flow {
        var offset = 0
        var items: List<T>
        var hasMore: Boolean
        var totalReceived = 0
        do {
            items = f(PageSpec(limit = pageSize + 1, offset = offset))
            val toTake = totalLimit
                ?.let { min(pageSize, totalLimit - totalReceived) }
                ?: pageSize
            emitAll(items.asFlow().take(toTake))
            totalReceived += toTake
            hasMore = items.size > pageSize
            offset += pageSize
        } while (hasMore && (totalLimit == null || totalReceived < totalLimit))
    }
}
