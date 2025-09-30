package io.github.palsergech.lib.spring.persistence.jdbc.latestsync

import io.github.palsergech.lib.platform.domain.persistence.LatestSync
import io.github.palsergech.lib.platform.domain.persistence.LatestSyncPersister
import java.time.Instant
import kotlin.jvm.optionals.getOrElse

internal class JdbcLatestSyncPersister(
    private val repository: LatestSyncRepository
) : LatestSyncPersister {

    override fun getLatestSync(entityType: String): LatestSync = repository
        .findById(entityType)
        .map { it.toDomain() }
        .getOrElse { LatestSync(timestampMs = Instant.EPOCH.toEpochMilli(), failedId = null) }

    override fun setLatestSync(entityType: String, timestampMs: Long, failedId: String?) {
        repository.save(entityType, timestampMs, failedId)
    }

    private fun LatestSyncRow.toDomain() = LatestSync(
        timestampMs = timestampMs,
        failedId = failedEntityId
    )
}
