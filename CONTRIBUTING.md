# How to Contribute

Thank you for your interest into contributing to the IntelliJ IDEA UI test library. Contributions are essential for expansion and growth of this project.


## First Time Setup
1) Install prerequisites:
   * [Java Development Kit](https://adoptopenjdk.net/)
2) Fork and clone the repository
3) Import the folder as a project in JetBrains IntelliJ IDEA


## Project Structure

This project is structured into 5 main parts.

### 1) Package <i>fixtures</i>
Package <i>fixtures</i> contains several predefined fixtures that can be instantiated and used to access and work with the IntelliJ IDEA UI elements such as wizards, buttons, inputs and other parts of the IntelliJ IDEA IDE.

### 2) Package <i>utils</i>

Package <i>utils</i> contains static methods and constants that can be useful for writing IntelliJ IDEA UI tests. There are for example methods for taking sceenshots or creating new project without the need of implementing the whole process using provided fixtures.

### 3) Package <i>exceptions</i>

Package <i>exceptions</i> contains predefined exceptions for handling situation such as the object that I am creating fixture for does not exists or there is unrelevant or unsupported option passed into a method.

### 4) Class <i>UITestRunner</i>

* Class contains static methods for starting and closing IntelliJ IDEA for UI tests
* Class contains public enum IdeaVersion defining supported IntelliJ IDEA versions
* Class contains other public methods to work with the IntelliJ IDEA
   * getIdeaVersion()
   * getRemoteRobot()
   * getRemoteRobotConnection()
   <br>...

### 5) The <i>test-project</i> folder

The <i>test-project</i> folder contains simple test project with JUnit 5 tests.



## Building the Library Locally

For building the library locally run the following command in the root of the project. The project will be built and installed into your .m2 local repository.

```
$ ./gradlew publishToMavenLocal
```


## Running Tests

To execute the JUnit 5 tests with **Community** version of IntelliJ run the following commands:

```
$ cd ./src/test-project
$ ./gradlew clean integrationUITest
```

To execute the JUnit 5 tests with **Ultimate** version of IntelliJ:
1. Decrypt **idea.key.gpg** file inside `./src/test-project/idea_license_key`
2. Run the following commands:
    ``` 
   $ cd ./src/test-project
   $ ./gradlew clean integrationUITestUltimate
   ```


## Reporting Issues

If you encounter a problem and know it is caused by the IntelliJ IDEA UI test library, please open an [issue report](https://github.com/redhat-developer/intellij-common-ui-test-library/issues). We really do appriciate any relevant issue report containg at least description and steps to reproduce the issue.


## License

This project is distributed under the Eclipse Public License - v 2.0 license. For more information see the [LICENSE](./LICENSE) file in the root of this repository.

### Certificate of Origin

By contributing to this project you agree to the Developer Certificate of
Origin (DCO). This document was created by the Linux Kernel community and is a
simple statement that you, as a contributor, have the legal right to make the
contribution. See the [DCO](DCO) file for details.
