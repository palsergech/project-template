import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id(Plugins.spring_boot) version PluginVers.spring_boot
    id(Plugins.open_api_generator) version PluginVers.open_api_generator
    application
}

dependencies {
    implementation(project(":db-migration"))

    implementation(Libs.springdoc_openapi_starter_webmvc_ui)
    implementation(Libs.openapitools_jackson_databind_nullable)

    implementation(Libs.spring_boot_starter_web)

    implementation(Libs.spring_boot_starter_actuator)
    implementation(Libs.micrometer_registry_prometheus)

    implementation(Libs.spring_boot_starter_amqp)
    implementation(Libs.spring_kafka)

    implementation(Libs.spring_aspects)
    implementation(Libs.spring_retry)

    implementation(Libs.spring_boot_starter_security)
    implementation(Libs.spring_security_oauth2_jose)
    implementation(Libs.spring_security_oauth2_resource_server)

    implementation(project(":modules:user-profile"))
    implementation(project(":modules:notes"))

    testImplementation(project(":db-migration"))
    testImplementation(project(":libs:spring-test"))
    testImplementation(Libs.spring_boot_starter_jdbc)
    testImplementation(Libs.ktor_client_core)
    testImplementation(Libs.ktor_client_cio_jvm)
    testImplementation(Libs.ktor_client_content_negotiation)
    testImplementation(Libs.ktor_serialization_jackson)
}

application {
    mainClass.set("io.github.palsergech.AppKt")
    applicationName = rootProject.name
}


sourceSets {
    main {
        java {
            srcDir(File(layout.buildDirectory.asFile.get(), "generated/src/main/kotlin"))
        }
        resources {
            srcDir(File("${project.rootDir}/api"))
        }
    }
}

tasks {


    val generateApiInterfaces by creating(GenerateTask::class) {
        dependsOn(named("processResources"))
        generatorName.set("kotlin-spring")
        configOptions.set(
            mapOf(
                "useSpringBoot3" to "true",
                "interfaceOnly" to "true",
                "useTags" to "true",
                "bigDecimalAsString" to "true",
                "gradleBuildFile" to "false",
                "reactive" to "true",
                "modelMutable" to "false",
                "apiFirst" to "true",
                "useSwaggerUI" to "false",
                "skipDefaultInterface" to "true",
                "enumPropertyNaming" to "UPPERCASE"
            )
        )
        ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
        apiPackage.set("io.github.palsergech.rest.api")
        modelPackage.set("io.github.palsergech.rest.dto")
        inputSpec.set(File(layout.buildDirectory.asFile.get(), "/resources/main/openapi.yaml").path)
        outputDir.set(layout.buildDirectory.asFile.get().resolve("generated").path)
        globalProperties.set(
            mapOf(
                "modelDocs" to "false",
            )
        )
    }

    compileKotlin {
        dependsOn(generateApiInterfaces)
    }
}