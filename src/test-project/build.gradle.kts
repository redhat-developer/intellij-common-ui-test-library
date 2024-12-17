import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version ("2.0.1")
}

group = "com.redhat.devtools.intellij"
version = "1.0-SNAPSHOT"
val platformVersion = providers.gradleProperty("ideaVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create(IntelliJPlatformType.IntellijIdeaCommunity, platformVersion)
        instrumentationTools()
    }
    testImplementation("com.redhat.devtools.intellij:intellij-common-ui-test-library")
    testImplementation("org.junit.platform:junit-platform-launcher:1.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks {
    test {
        systemProperty("intellij_debug", "true")
        useJUnitPlatform()
    }

    register("copyKey", Copy::class.java) {
        from("idea_license_token/idea.key")
        into("build/idea-sandbox/config-uiTest")
    }
}

val integrationUITestUltimate by intellijPlatformTesting.testIde.registering {
    task {
        systemProperty("intellij_debug", "true")
        group = "verification"
        useJUnitPlatform()
        dependsOn(tasks["copyKey"])
    }
}

val integrationUITest by intellijPlatformTesting.testIde.registering {
    task {
        systemProperty("intellij_debug", "true")
        group = "verification"
        useJUnitPlatform()
    }
}

// https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#runIdeForUiTests
val runIdeForUiTests by intellijPlatformTesting.runIde.registering {
    task {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf(
                "-Dide.mac.message.dialogs.as.sheets=false",
                "-Djb.privacy.policy.text=<!--999.999-->",
                "-Djb.consents.confirmation.enabled=false",
                "-Dide.mac.file.chooser.native=false",
                "-DjbScreenMenuBar.enabled=false",
                "-Dapple.laf.useScreenMenuBar=false",
                "-Didea.trust.all.projects=true",
                "-Dide.show.tips.on.startup.default.value=false",
            )
        }
    }
    plugins {
        robotServerPlugin()
    }
}

