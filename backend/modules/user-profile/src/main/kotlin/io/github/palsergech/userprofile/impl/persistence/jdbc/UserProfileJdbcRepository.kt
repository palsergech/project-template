package io.github.palsergech.userprofile.impl.persistence.jdbc

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Table("user_profile")
internal class UserProfileRow(
    @Id
    val id: UUID,
    val name: String?,
    val email: String?,
    @Version
    val version: Int,
)

@Repository
internal interface UserProfileJdbcRepository: CrudRepository<UserProfileRow, UUID> {

}