package io.github.palsergech.userprofile.usecases

import io.github.palsergech.lib.platform.domain.Id
import io.github.palsergech.userprofile.domain.UserProfile
import io.github.palsergech.userprofile.domain.UserProfilePatch
import io.github.palsergech.userprofile.domain.events.UserProfileUpdatedEvent
import io.github.palsergech.userprofile.impl.persistence.jdbc.UserProfileJdbcRepository
import io.github.palsergech.userprofile.impl.persistence.jdbc.UserProfileRow
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull


@Service
class UserService internal constructor(
    private val repository: UserProfileJdbcRepository,
    private val kafkaTemplate: KafkaTemplate<String, UserProfileUpdatedEvent>
) {

    fun getUserProfile(userId: Id<UserProfile>): UserProfile {
        return repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?: UserProfile.create(userId)
    }

    @Transactional
    @Retryable(
        retryFor = [OptimisticLockingFailureException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 50, multiplier = 2.0, random = true),
    )
    fun patchUserProfileWithRetry(userId: Id<UserProfile>, patch: UserProfilePatch): UserProfile {
        val user = repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?.applyPatch(patch)
            ?: UserProfile.create(userId, patch)
        repository.save(user.toRow())
        kafkaTemplate.publish(user.toEvent())
        return user
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun patchUserProfile(userId: Id<UserProfile>, patch: UserProfilePatch): UserProfile {
        val user = repository.findById(userId.value)
            .getOrNull()
            ?.toDomain()
            ?.applyPatch(patch)
            ?: UserProfile.create(userId, patch)
        repository.save(user.toRow())
        kafkaTemplate.publish(user.toEvent())
        return user
    }


    private fun KafkaTemplate<String, UserProfileUpdatedEvent>.publish(event: UserProfileUpdatedEvent) {
        send("user-events", event.id.toString(), event)
    }

    private fun UserProfile.toEvent(): UserProfileUpdatedEvent {
        return UserProfileUpdatedEvent(
            id = id.value,
            name = name,
            email = email
        )
    }

    private fun UserProfileRow.toDomain(): UserProfile {
        return UserProfile(
            id = UserProfile.idFrom(id),
            name = name,
            email = email,
            version = version
        )
    }

    private fun UserProfile.toRow(): UserProfileRow {
        return UserProfileRow(
            id = id.value,
            name = name,
            email = email,
            version = version
        )
    }
}