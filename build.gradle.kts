plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.gradleNexusPublishPlugin)
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
    api(libs.junit.jupiter.api)
    api(libs.remote.robot)
    api(libs.remote.fixtures)
}


java {
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

    sonar {
        properties {
            property("sonar.projectKey", "redhat-developer_intellij-common-ui-test-library")
            property("sonar.organization", "redhat-developer")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.sources", "src")
            //property("sonar.gradle.skipCompile", "true")
        }
    }

}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named("sourcesJar"))
            artifact(tasks.named("javadocJar"))
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
}

nexusPublishing {
    packageGroup.set("JBoss Releases Staging Profile")
    repositories {
        create("jbossNexus") {
            nexusUrl.set(uri("https://repository.jboss.org/nexus/service/local/"))
            snapshotRepositoryUrl.set(uri("https://repository.jboss.org/nexus/content/repositories/snapshots/"))
            username.set(project.properties["nexusUser"].toString()) // defaults to project.properties["myNexusUsername"]
            password.set(project.properties["nexusPassword"].toString()) // defaults to project.properties["myNexusPassword"]
        }
    }
}
