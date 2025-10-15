package io.github.palsergech.userprofile

import io.github.palsergech.userprofile.usecases.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@Import(
    UserService::class
)
@EnableJdbcRepositories("io.github.palsergech.userprofile.impl.persistence.jdbc")
class UserProfileModuleConfiguration
