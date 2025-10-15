package io.github.palsergech.userprofile.usecases

import io.github.palsergech.lib.platform.domain.Id
import io.github.palsergech.userprofile.domain.UserProfile
import io.github.palsergech.userprofile.domain.UserProfilePatch
import io.github.palsergech.userprofile.impl.persistence.jdbc.UserProfileJdbcRepository
import io.github.palsergech.userprofile.impl.persistence.jdbc.UserProfileRow
import kotlin.jvm.optionals.getOrNull

class UserService internal constructor(
    private val repository: UserProfileJdbcRepository
) {

    fun getUserProfile(userId: Id<UserProfile>): UserProfile {
        return repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?: UserProfile.default(userId)
    }

    fun patchUserProfile(userId: Id<UserProfile>, patch: UserProfilePatch): UserProfile {
        val user = repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?: UserProfile.default(userId).also {
                repository.save(it.toRow().apply { setNew() })
            }
        val updated = user
            .applyPatch(patch)
        repository.save(updated.toRow())
        return updated
    }

    fun updateUserProfile(userId: Id<UserProfile>, name: String?, email: String?): UserProfile {
        val user = repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?: UserProfile.default(userId).also {
                repository.save(it.toRow().apply { setNew() })
            }
        val updated = user.update(name = name, email = email)
        repository.save(updated.toRow())
        return updated
    }

    private fun UserProfile.toRow(): UserProfileRow {
        return UserProfileRow(
            entityId = id.value,
            name = name,
            email = email
        )
    }

    private fun UserProfileRow.toDomain(): UserProfile {
        return UserProfile(
            id = UserProfile.idFrom(entityId),
            name = name,
            email = email
        )
    }
}