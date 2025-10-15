package io.github.palsergech.userprofile.impl.persistence.jdbc

import io.github.palsergech.lib.spring.persistence.jdbc.EntityWithManualId
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Table("user_profile")
internal class UserProfileRow(
    entityId: UUID,
    val name: String?,
    val email: String?
): EntityWithManualId<UUID>(entityId)

@Repository
internal interface UserProfileJdbcRepository: CrudRepository<UserProfileRow, UUID>