dependencies {
    api(project(":libs:platform"))

    api(Libs.spring_boot_starter_test)
    api(Libs.assertk)
    api("org.wiremock:wiremock-standalone:3.3.1")
    api("io.mockk:mockk:1.13.10")

    api("com.jayway.jsonpath:json-path:2.9.0")
    api("net.javacrumbs.json-unit:json-unit-assertj:2.36.1")

    api(Libs.spring_boot_starter_test)
    api(Libs.assertk)
    api("org.wiremock:wiremock-standalone:3.3.1")
    api("io.mockk:mockk:1.13.10")
    implementation(Libs.postgresql)
    implementation(Libs.flyway_core)
    implementation(Libs.testcontainers_core)
    implementation(Libs.testcontainers_junit)
    implementation(Libs.testcontainers_postgresql)
    implementation(Libs.spring_boot_starter_data_jdbc)
}
