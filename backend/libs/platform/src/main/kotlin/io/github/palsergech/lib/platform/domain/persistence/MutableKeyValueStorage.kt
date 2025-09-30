package io.github.palsergech.lib.platform.domain.persistence

interface MutableKeyValueStorage<in K : Any, V> {
    operator fun get(key: K): V?

    operator fun set(key: K, value: V)
}
