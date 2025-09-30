package io.github.palsergech.lib.platform.domain

open class DomainError(override val message: String): RuntimeException(message)

class ObjectNotFoundError(val objectType: String, val key: Key)
    : DomainError("$objectType with key $key not found") {

    sealed interface Key
    data class IdKey(val id: Id<*>): Key {
        override fun toString(): String = "id=$id"
    }
}

class ObjectAlreadyExistsError(objectType: String, key: String, keyType: String = "key")
    : DomainError("""$objectType with $keyType '$key' already exists""")
