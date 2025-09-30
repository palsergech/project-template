package io.github.palsergech.rest

import io.github.palsergech.rest.api.GreetingApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController: GreetingApi {

    override suspend fun getGreeting(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World!")
    }
}