package io.github.palsergech.module1.integration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import io.github.palsergech.module1.Module1Configuration
import io.github.palsergech.users.usecases.UserQueries
import io.mockk.mockk
import org.springframework.context.annotation.Bean

@TestConfiguration
@Import(
    Module1Configuration::class
)
internal class Module1TestConfiguration {

}
