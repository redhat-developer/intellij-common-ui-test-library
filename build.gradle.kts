plugins {
    id("java-library")
    id("maven-publish")
    id("jacoco") // Code coverage
    alias(libs.plugins.sonarqube) // SonarQube
}

group = "com.redhat.devtools.intellij"
version = providers.gradleProperty("projectVersion").get() // Plugin version

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.openapi)
    api(libs.junit.jupiter.api)
    api(libs.remote.robot)
    api(libs.remote.fixtures)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        executionData.setFrom(fileTree(layout.buildDirectory).include("/jacoco/*.exec"))
        reports {
            xml.required = true
        }
    }

    sonar {
        properties {
            property("sonar.projectKey", "redhat-developer_intellij-common-ui-test-library")
            property("sonar.organization", "redhat-developer")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.junit.reportsPath", layout.buildDirectory.dir("test-results").get().asFile.absolutePath)
            property("sonar.gradle.skipCompile", "true")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("IntelliJ common UI test library")
                description.set("Common utilities for IntelliJ UI testing")
                url.set("https://github.com/redhat-developer/intellij-common-ui-test-library")

                licenses {
                    license {
                        name.set("Eclipse Public License 2.0")
                        url.set("https://www.eclipse.org/legal/epl-v20.html")
                    }
                }
                developers {
                    developer {
                        name.set("Red Hat Developer")
                        email.set("devtools-team@redhat.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/redhat-developer/intellij-common-ui-test-library.git")
                    developerConnection.set("scm:git:ssh://git@github.com:redhat-developer/intellij-common-ui-test-library.git")
                    url.set("https://github.com/redhat-developer/intellij-common-ui-test-library/")
                }
            }
        }
    }
    repositories {
        maven {
            val baseUrl = layout.buildDirectory.dir("repository/").get()
            val releasesRepoUrl = baseUrl.dir("releases")
            val snapshotsRepoUrl = baseUrl.dir("snapshots")
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
}
