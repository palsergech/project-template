package io.github.palsergech.rest

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    GreetingController::class,
    UserProfileController::class,

    ApiExceptionHandler::class,
)
internal class RestConfiguration

