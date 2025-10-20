package io.github.palsergech.userprofile.integration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import io.github.palsergech.userprofile.Module1Configuration

@TestConfiguration
@Import(
    Module1Configuration::class
)
internal class Module1TestConfiguration {

}
