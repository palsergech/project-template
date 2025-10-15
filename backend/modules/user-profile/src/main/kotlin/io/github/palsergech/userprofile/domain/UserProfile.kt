package io.github.palsergech.userprofile.domain

import io.github.palsergech.lib.platform.domain.Id
import io.github.palsergech.lib.platform.domain.IdFactory

data class UserProfile(
    val id: Id<UserProfile>,
    val name: String?,
    val email: String?
) {
    companion object: IdFactory<UserProfile> {
        fun default(id: Id<UserProfile>) = UserProfile(
            id = id,
            name = null,
            email = null
        )
    }

    fun update(
        name: String?,
        email: String?
    ): UserProfile {
        return copy(
            name = name,
            email = email
        )
    }

    fun applyPatch(patch: UserProfilePatch): UserProfile {
        return copy(
            name = patch.name ?: name,
            email = patch.email ?: email
        )
    }
}

data class UserProfilePatch(
    val name: String?,
    val email: String?
)