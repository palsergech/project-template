package io.github.palsergech.userprofile.domain

import io.github.palsergech.lib.platform.domain.Id
import io.github.palsergech.lib.platform.domain.IdFactory

data class UserProfile(
    val id: Id<UserProfile>,
    val name: String?,
    val email: String?,
    val version: Int
) {
    companion object: IdFactory<UserProfile> {
        fun create(id: Id<UserProfile>, patch: UserProfilePatch? = null) = UserProfile(
            id = id,
            name = patch?.name,
            email = patch?.email,
            version = 0
        )
    }

    fun applyPatch(patch: UserProfilePatch): UserProfile {
        return copy(
            name = patch.name ?: name,
            email = patch.email ?: email,
            version = this.version
        )
    }
}

data class UserProfilePatch(
    val name: String?,
    val email: String?
)