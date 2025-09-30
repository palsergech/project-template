val parentProjectDir = projectDir

plugins {
    id(Plugins.kotlin) version PluginVers.kotlin
    id(Plugins.spring_kotlin) version PluginVers.spring_kotlin apply false
    id(Plugins.spring_dependency_management) version PluginVers.spring_dependency_management
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

allprojects {

    configurations.all {
        resolutionStrategy {
            eachDependency {
                requested.version?.contains("snapshot", true)?.let {
                    if (it) {
                        throw GradleException("Snapshot found: ${requested.name} ${requested.version}")
                    }
                }
            }
        }
    }

    apply {
        plugin(Plugins.kotlin)
        plugin(Plugins.javaTestFixtures)
        plugin(Plugins.spring_kotlin)
        plugin(Plugins.spring_dependency_management)
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(Libs.kotlin_jdk8)
        implementation(Libs.kotlin_reflect)
        implementation(Libs.kotlin_coroutines)
        implementation(Libs.kotlin_coroutines_reactor)

        implementation(Libs.jackson_kotlin)

        implementation(Libs.slf4j_api)

        testImplementation(Libs.junit_engine)
        testImplementation(Libs.assertk)
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${LibVers.spring_boot}")
        }
    }
}
