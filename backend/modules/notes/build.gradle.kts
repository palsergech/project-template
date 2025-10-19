dependencies {
    api(project(":libs:platform"))

    implementation(Libs.spring_boot_starter_data_jdbc)
    implementation(Libs.postgresql)

    testImplementation(project(":db-migration"))
    testImplementation(project(":libs:spring-test"))
}
