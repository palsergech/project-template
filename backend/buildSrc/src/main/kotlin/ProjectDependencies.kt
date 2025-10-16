object LibVers {
    const val spring_boot = "3.2.2"
    const val swagger = "3.0.0"
    const val commons_net = "3.8.0"
    const val testcontainers = "1.16.2"
    const val wiremock = "2.31.0"
    const val rest_assured = "4.4.0"
    const val corounit = "1.1.1"
    const val spring_rabbit_test = "2.4.0"
    const val springdoc_openapi_starter_webmvc_ui = "2.2.0"
    const val openapitools_jackson_databind_nullable = "0.2.1"
    const val web3j = "4.8.7"
    const val assertk = "0.28.0"
}

object Libs {
    // Kotlin
    const val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Global.kotlin_coroutines_version}"
    const val kotlin_coroutines_reactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Global.kotlin_coroutines_version}"
    const val kotlin_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Global.kotlin_version}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin_version}"

    // ktor-client
    const val ktor_client_core = "io.ktor:ktor-client-core:${Global.ktor_version}"
    const val ktor_client_cio_jvm = "io.ktor:ktor-client-cio-jvm:${Global.ktor_version}"
    const val ktor_client_content_negotiation = "io.ktor:ktor-client-content-negotiation:${Global.ktor_version}"
    const val ktor_serialization_jackson = "io.ktor:ktor-serialization-jackson:${Global.ktor_version}"
    const val ktor_client_auth = "io.ktor:ktor-client-auth:${Global.ktor_version}"
    const val ktor_client_logging = "io.ktor:ktor-client-logging:${Global.ktor_version}"

    // Jackson
    const val jackson_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val jackson_databind = "com.fasterxml.jackson.core:jackson-databind"

    // OpenApi
    const val springdoc_openapi_starter_webmvc_ui = "org.springdoc:springdoc-openapi-starter-webmvc-ui:${LibVers.springdoc_openapi_starter_webmvc_ui}"
    const val openapitools_jackson_databind_nullable = "org.openapitools:jackson-databind-nullable:${LibVers.openapitools_jackson_databind_nullable}"

    //web3
    const val web3j = "org.web3j:core:${LibVers.web3j}"

    // Spring
    const val spring_core = "org.springframework:spring-core"
    const val spring_context = "org.springframework:spring-context"
    const val spring_kafka = "org.springframework.kafka:spring-kafka"
    const val spring_boot_dev_tools = "org.springframework.boot:spring-boot-devtools"
    const val spring_boot = "org.springframework.boot:spring-boot"
    const val spring_boot_starter_web = "org.springframework.boot:spring-boot-starter-web"
    const val spring_boot_starter_jdbc = "org.springframework.boot:spring-boot-starter-jdbc"
    const val spring_boot_starter_data_jdbc = "org.springframework.boot:spring-boot-starter-data-jdbc"
    const val spring_boot_starter_logging = "org.springframework.boot:spring-boot-starter-logging"
    const val spring_boot_starter_test = "org.springframework.boot:spring-boot-starter-test"
    const val spring_boot_starter_thymeleaf = "org.springframework.boot:spring-boot-starter-thymeleaf"
    const val spring_boot_started_hateoas = "org.springframework.boot:spring-boot-starter-hateoas"
    const val spring_boot_starter_amqp = "org.springframework.boot:spring-boot-starter-amqp"
    const val spring_boot_starter_security = "org.springframework.boot:spring-boot-starter-security"
    const val spring_boot_starter_actuator = "org.springframework.boot:spring-boot-starter-actuator"
    const val spring_security_oauth2_resource_server = "org.springframework.security:spring-security-oauth2-resource-server"
    const val spring_security_oauth2_jose = "org.springframework.security:spring-security-oauth2-jose"

    const val swagger = "io.springfox:springfox-boot-starter"
    const val swagger_ui = "io.springfox:springfox-swagger-ui"

    // Monitoring
    const val micrometer_registry_prometheus = "io.micrometer:micrometer-registry-prometheus:1.12.5"

    // Logging
    const val slf4j_api = "org.slf4j:slf4j-api"

    // Tests
    const val junit_params = "org.junit.jupiter:junit-jupiter-params"
    const val junit_engine = "org.junit.jupiter:junit-jupiter-engine"
    const val kotest_junit = "io.kotest:kotest-runner-junit5"
    const val testcontainers_junit = "org.testcontainers:junit-jupiter:${LibVers.testcontainers}"
    const val testcontainers_postgresql = "org.testcontainers:postgresql:${LibVers.testcontainers}"
    const val testcontainers_rabbit = "org.testcontainers:rabbitmq:${LibVers.testcontainers}"
    const val testcontainers_core = "org.testcontainers:testcontainers:${LibVers.testcontainers}"
    const val wiremock = "com.github.tomakehurst:wiremock-jre8:${LibVers.wiremock}"
    const val rest_assured = "io.rest-assured:rest-assured:${LibVers.rest_assured}"
    const val rest_assured_kotlin = "io.rest-assured:kotlin-extensions:${LibVers.rest_assured}"
    const val spring_rabbit_test = "org.springframework.amqp:spring-rabbit-test"
    const val assertk = "com.willowtreeapps.assertk:assertk-jvm:${LibVers.assertk}"

    // Database
    const val postgresql = "org.postgresql:postgresql"
    const val flyway_core = "org.flywaydb:flyway-core"

    //Scheduling
    const val db_scheduler = "com.github.kagkarlsson:db-scheduler-spring-boot-starter:15.1.1"
}

object PluginVers {
    const val kotlin = Global.kotlin_version
    const val open_api_generator = "7.2.0"
    const val spring_boot = LibVers.spring_boot
    const val detekt = "1.23.3"
    const val detekt_formatting = detekt
    const val spring_dependency_management = "1.1.4"
    const val spring_kotlin = Global.kotlin_version
    const val update_dependencies = "0.36.0"
    const val owasp_dependencies = "6.1.0"
    const val pitest = "1.7.0"
    const val allure = "2.9.6"
    const val allure_cli = "2.15.0"
    const val allure_java = "2.15.0"
    const val gatling = "3.9.5.6"
}

object Plugins {
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val open_api_generator = "org.openapi.generator"

    const val spring_boot = "org.springframework.boot"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val spring_dependency_management = "io.spring.dependency-management"
    const val spring_kotlin = "org.jetbrains.kotlin.plugin.spring"
    const val update_dependencies = "com.github.ben-manes.versions"
    const val owasp_dependencies = "org.owasp.dependencycheck"
    const val pitest = "info.solidsoft.pitest"
    const val javaTestFixtures = "java-test-fixtures"
    const val allure = "io.qameta.allure"
    const val gatling = "io.gatling.gradle"
}
