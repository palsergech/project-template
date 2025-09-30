package io.github.palsergech.lib.platform.collections

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.math.BigDecimal
import kotlin.random.Random.Default.nextInt

suspend fun <T, R> Iterable<T>.coMap(f: suspend (T) -> R): List<R> {
    return asFlow().map(f).toList()
}

suspend fun <T, R> Array<T>.coMap(f: suspend (T) -> R): List<R> {
    return asFlow().map(f).toList()
}

fun <T, S> Iterable<T>.allUniqueBy(selector: (T) -> S): Boolean {
    val uniqueElems = mutableSetOf<S>()
    forEach { t ->
        val s = selector(t)
        if (uniqueElems.contains(s)) return false
        uniqueElems.add(s)
    }

    return true
}

fun <T> Iterable<T>.allUnique(): Boolean = allUniqueBy { it }

fun <T> Collection<T>.takeRandomly(n: Int): List<T>  {
    require(n >= 0) { "Requested element count $n is less than zero." }
    require(n <= size) { "Requested element count $n is greater than collection size." }

    return shuffled().take(n)
}

fun <T> Collection<T>.takeRandomly(isEmptyAllowed: Boolean = true): List<T> {
    require(isEmptyAllowed || isNotEmpty()) { "Cant get not empty sublist for empty collection." }

    return takeRandomly(
        nextInt(
            from = if (isEmptyAllowed) 0 else 1,
            until = size + 1
        )
    )
}

fun <T, R> Iterable<T>.flatMapToSet(f: (T) -> Iterable<R>): Set<R> {
    return flatMapTo(mutableSetOf(), f)
}

fun <T, R> Iterable<T>.mapToSet(f: (T) -> R): Set<R> {
    return mapTo(mutableSetOf(), f)
}

fun <T, R : Any> Iterable<T>.mapNotNullToSet(f: (T) -> R?): Set<R> {
    return mapNotNullTo(mutableSetOf(), f)
}

fun Iterable<BigDecimal>.sum() = sumOf { it }

fun <K : Any, V : Any> Map<K, V?>.filterNotNullValues(): Map<K, V> {
    return mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
}

fun <T> Iterable<T>.intersects(other: Iterable<T>): Boolean {
    val (lesser, greater) = if (this is Collection<*> && other is Collection<*>) {
        listOf(this, other).sortedBy { it.size }
    } else listOf(this, other)

    for (e in lesser) {
        if (e in greater) return true
    }
    return false
}
