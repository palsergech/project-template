dependencies {
    implementation(project(":libs:platform"))

    implementation(Libs.spring_boot_starter_web)
    implementation(Libs.spring_boot_starter_amqp)
    implementation(Libs.spring_boot_starter_actuator)
    implementation(Libs.micrometer_registry_prometheus)

    implementation(Libs.spring_boot_starter_data_jdbc)
    implementation(Libs.spring_boot_starter_security)
    implementation(Libs.postgresql)
    implementation(Libs.flyway_core)

    api(Libs.db_scheduler)
}
