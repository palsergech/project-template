package io.github.palsergech.lib.spring.persistence.jdbc.latestsync

import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Table("last_sync")
internal data class LatestSyncRow(
    @Id
    val entityType: String,
    val timestampMs: Long,
    val failedEntityId: String?
)

@Repository
internal interface LatestSyncRepository : CrudRepository<LatestSyncRow, String> {
    @Modifying
    @Query("""
        insert into last_sync (entity_type, timestamp_ms, failed_entity_id)
        values (:entityType, :timestampMs, :failedEntityId)
        on conflict (entity_type) do update set timestamp_ms = :timestampMs, failed_entity_id = :failedEntityId
    """)
    fun save(entityType: String, timestampMs: Long, failedEntityId: String?)
}
