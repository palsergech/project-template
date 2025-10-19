package io.github.palsergech.userprofile.integration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@Import(
    UserProfileModuleConfiguration::class
)
internal class Module1TestConfiguration {

}
