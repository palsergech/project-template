package io.github.palsergech.lib.platform.domain.persistence

interface LatestSyncPersister : MutableKeyValueStorage<String, LatestSync> {
    fun getLatestSync(entityType: String): LatestSync

    fun setLatestSync(entityType: String, timestampMs: Long, failedId: String? = null)

    override operator fun get(key: String) = getLatestSync(key)

    override operator fun set(key: String, value: LatestSync) = setLatestSync(key, value.timestampMs, value.failedId)
}

data class LatestSync(
    val timestampMs: Long,
    val failedId: String?
)
