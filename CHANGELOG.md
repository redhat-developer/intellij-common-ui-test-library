# Change Log

## 0.4.1 (Jul 30, 2024)
- Add platformType and platformVersion to ProcessBuilder parameters [(#239)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/239)
- Fix Project creation failing on GHA [(#260)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/260)
- Add create EMPTY PROJECT methods [(#275)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/275)
- Update dependencies

## 0.4.0 (May 7, 2024)
- Add support for IntelliJ 2024.1
- Update the `Remote-Robot` to version 0.11.22

- ## 0.3.0 (Feb 15, 2024)
 - Add support for IntelliJ CE 2023.2
 - Added custom test-project location
 - Update the `Remote-Robot` to version 0.11.18
 - Update Java to JDK 17
 - Update Gradle to version 7.6
 - Removed Redundancy in the ToolWindowsPane class
 - Added method for reopening project
 - Added possibility to redirect remote-robot logs to output files in user.dir/intelliJ_debug 

## 0.2.0 (Aug 4, 2022)
 - Add support for IntelliJ CE 2022.2

## 0.1.1 (Aug 2, 2022)
 - Update the `Remote-Robot` to version 0.11.15
 - Issues fixed:
   - Typo in `utils.constan(t)s`
   - Not working `Sonar analysis` in PRs
   - The `TimeoutException` while working with the gradle tasks tree (JTreeFixture) on OS X

## 0.1.0 (Jun 24, 2022)
 - Add DCO documentation
 - Add support for IntelliJ IDEA 2022.1

## 0.0.9 (May 10, 2022)
 - Add support for IntelliJ IDEA Ultimate (2020.2, 2020.3, 2021.1, 2021.2)
 - Add support for the following IntelliJ IDEA versions: 
   - IntelliJ IDEA 2021.1 (Community Edition)
   - IntelliJ IDEA 2021.2 (Community Edition)
   - IntelliJ IDEA 2021.3 (Community Edition)

## 0.0.8 (Mar 7, 2022)
 - Fix `java.net.ConnectException when closing IntelliJ IDEA` issue

## 0.0.7 (Feb 25, 2022)
 - Update the `Remote-Robot` version to 0.11.13
 - Fix `multiple screenshots overwriting each other` issue

## 0.0.6 (Feb 5, 2022)
 - Update kotlin-stdlib and kotlin-reflect to version 1.6.0
 - Add the CHANGELOG.md file

## 0.0.5 (Jan 24, 2022)
 - Update the `Remote-Robot` version to 0.11.11
 - Add support for starting the `IntelliJ IDEA Ultimate`

## 0.0.4 (Nov 15, 2021)
 - Update the `Remote-Robot` version to 0.11.8
 - Fix the `SonarCloud` run for PRs
 - Fix the issues reported by the `SonarCloud`
 - Update the `README.md` file
 - Refactoring

## 0.0.3 (Oct 23, 2021)
 - Add a code quality gating solution - `SonarCloud`
 - Add the contribution guide (the `CONTRIBUTING.md` file)
 - Add the license (the `LICENSE` file)
 - Integrate the `Remote-Robot` step logging solution
 - Update the `Remote-Robot` version to 0.11.7
 - Create new fixtures for:
   - `New Project` (with appropriate pages)
   - `Project Explorer` 
   - `Build Tools Pane`
   - `Project Structure`
 - Refactoring
   - Eliminate duplicate implementations of repeating test steps
   - Rework existing custom fixtures to utilise existing remote-fixtures API
   - Move the `test-project` folder to `./src/test-project`
