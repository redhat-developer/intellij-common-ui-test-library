# intellij-common-ui-test-library

### Add this library to an existing IntelliJ Plugin project

1) Generate pom.xml and install the artifact into your local maven repository
```
./gradlew createPom && mvn clean install
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
```

3) Run and close the IntelliJ Idea before and after all the UI tests
```
private static RemoteRobot robot;

@BeforeAll
public static void runIdeForUiTests() {
    robot = IntegrationTestsUtils.runIde("IC-2020.2", 8082);
}

@AfterAll
public static void closeIde() {
    IntegrationTestsUtils.closeIde();
}
```