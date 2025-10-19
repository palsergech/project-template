package io.github.palsergech.rest

import io.github.palsergech.rest.api.UserProfileApi
import io.github.palsergech.rest.dto.PatchUserProfileReqDTO
import io.github.palsergech.rest.dto.UserProfileDTO
import io.github.palsergech.security.AuthExtractor
import io.github.palsergech.userprofile.domain.UserProfile
import io.github.palsergech.userprofile.domain.UserProfilePatch
import io.github.palsergech.userprofile.usecases.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserProfileController(
    private val userService: UserService
): UserProfileApi {

    override suspend fun getUserProfile(): ResponseEntity<UserProfileDTO> {
        val id = UserProfile.idFrom(AuthExtractor.getCurrentUserId())
        val user = userService.getUserProfile(id)
        return ResponseEntity.ok(user.toDTO())
    }

    override suspend fun patchUserProfile(req: PatchUserProfileReqDTO, retry: Boolean): ResponseEntity<UserProfileDTO> {
        val id = UserProfile.idFrom(AuthExtractor.getCurrentUserId())
        val user = if (retry) {
            userService.patchUserProfileWithRetry(id, req.toPatch())
        } else {
            userService.patchUserProfile(id, req.toPatch())
        }
        return ResponseEntity.ok(user.toDTO())
    }

    private fun UserProfile.toDTO(): UserProfileDTO {
        return UserProfileDTO(
            id = id.value,
            name = name,
            email = email
        )
    }

    private fun PatchUserProfileReqDTO.toPatch(): UserProfilePatch {
        return UserProfilePatch(
            name = name,
            email = email
        )
    }
}