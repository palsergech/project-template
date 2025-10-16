package io.github.palsergech.userprofile.domain.events

import io.github.palsergech.lib.platform.domainevents.DomainEvent
import java.util.UUID

data class UserProfileUpdatedEvent(
    val id: UUID,
    val name: String?,
    val email: String?,
    override val timestampMs: Long = System.currentTimeMillis()
): DomainEvent {
    override val type: String = "user-profile-updated"
}