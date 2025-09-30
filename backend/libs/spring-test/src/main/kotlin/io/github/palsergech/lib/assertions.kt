package io.github.palsergech.lib

import assertk.Assert
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder

inline fun <reified T> Assert<List<T>>.containsExactlyInAnyOrderElementsOf(iterable: Iterable<T>) {
    containsExactlyInAnyOrder(*iterable.toList().toTypedArray())
}

inline fun <reified T: Any> Assert<List<T>>.containsExactlyElementsOf(iterable: Iterable<T>) {
    containsExactly(*iterable.toList().toTypedArray())
}
