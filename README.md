# intellij-common-ui-test-library

### Add this library to an existing IntelliJ Plugin project

1) Install the plugin to your local maven repository (use Java 8 to run the following cmd)
```
./gradlew publishToMavenLocal
```

2) Extend the build.gradle file of the IntelliJ Plugin repo

```
sourceSets {
    integrationTest {
        java.srcDir file('src/it/java')
        resources.srcDir file('src/it/resources')
        compileClasspath += sourceSets.main.output + configurations.testRuntime
        runtimeClasspath += output + compileClasspath
    }
}

task integrationTest(type: Test) {
    useJUnitPlatform()
    description = 'Runs the integration tests.'
    group = 'verification'
    testClassesDirs = sourceSets.integrationTest.output.classesDirs
    classpath = sourceSets.integrationTest.runtimeClasspath
    outputs.upToDateWhen { false }
    mustRunAfter test
}

dependencies {
    compile 'org.jboss.tools.intellij.commonUiTestLibrary:intellij-common-ui-test-library:0.0.1-SNAPSHOT'
}

runIdeForUiTests {
    systemProperty "robot-server.port", System.getProperty("robot-server.port")
}

repositories {
    maven {
        url 'https://packages.jetbrains.team/maven/p/ij/intellij-dependencies'
    }
}
```

3) Run and close the IntelliJ Idea before and after all the UI tests
```
private static RemoteRobot robot;

@BeforeAll
public static void runIdeForUiTests() {
    robot = UITestRunner.runIde("IC-2020.2", 8082);
}

@AfterAll
public static void closeIde() {
    UITestRunner.closeIde();
}
```

### Run JUnit tests to test this library

You can run JUnit tests contained in this repository to test this library by executing the following command:

```sh
cd test-project
./gradlew test
```
