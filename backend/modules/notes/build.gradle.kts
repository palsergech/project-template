dependencies {
    api(project(":libs:platform"))
    api(project(":libs:platform-spring"))

    implementation(Libs.spring_boot_starter_data_jdbc)
    implementation(Libs.postgresql)

    testImplementation(project(":db-migration"))
    testImplementation(project(":libs:spring-test"))
}
