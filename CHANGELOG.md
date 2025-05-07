# Change Log

## 0.4.4 (May 7, 2025)
- chore(deps): bump org.junit.jupiter:junit-jupiter-api [`#339`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/339)
- chore(deps): bump remote-robot from 0.11.22 to 0.11.23 [`#329`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/329)
- chore: fix build [`#343`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/343)
- build: migrate from jenkins/nexus to github action/github maven repo [`#341`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/341)
- fix: fix failed tests screenshot comment  [`#335`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/335)
- chore: fix sonar issues [`#333`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/333)
- Integration tests fix [`#332`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/332)
- Integration tests fix [`#331`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/331)
- chore: fix integration tests [`#327`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/327)
- chore: fix publishing [`#328`](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/328)
- chore: bump version [`a94c6cd`](https://github.com/redhat-developer/intellij-common-ui-test-library/commit/a94c6cde6ac97645e6bfee99f1c8fe5a13fea185)
- chore: Update sonar.yml [`191c03b`](https://github.com/redhat-developer/intellij-common-ui-test-library/commit/191c03be187a34ec5f51ba5bce91a30f6f8f4ff3)
- chore: fixed nightly [`c166204`](https://github.com/redhat-developer/intellij-common-ui-test-library/commit/c16620403c4e4ecc08381e3903baf87661c237d2)

## 0.4.3 (Nov 4, 2024)
- chore(deps): bump org.jetbrains.kotlin:kotlin-reflect (#316)
- chore(deps): bump org.jetbrains.kotlin.jvm from 2.0.20 to 2.0.21 (#315)
- chore(deps): bump org.junit.jupiter:junit-jupiter-engine (#314)
- chore(deps): bump org.junit.jupiter:junit-jupiter-api (#313)
- chore(deps): bump commons-io:commons-io from 2.16.1 to 2.17.0 (#307)
- Merge pull request #312 from redhat-developer/0.4.3-bump
- chore: fix publish and bump version
- feat: added IU-2024.2 to IntelliJVersion (#302)

## 0.4.2 (Sep 17, 2024)
 - fix: IdeaProject folder getting deleted by running local tests fixed #293 by @martinszuc in [(#294)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/294)
 - fix(UITest): fix for unstable tests & minor improvements #212 & #296 by @martinszuc in [(#301)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/301)
 - fix(UITest): fix for unstable MenuBar tests #278 by @martinszuc in [(#295)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/295)
 - Integrate the UI Test Robot properties inside test-project #256 by @richardkocian in [(#282)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/295)
 - waitForDialogsToDisappear method for empty project creation process #284 by @martinszuc in [(#285)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/285)
 - feat: Add UI integration tests for IntelliJ Ultimate #240 by @richardkocian in  [(#286)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/286)
 - chore(deps): bump org.jetbrains.kotlin:kotlin-reflect from 2.0.0 to 2.0.20 in [(#298)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/298)
 - chore(deps): bump org.jetbrains.kotlin.jvm from 2.0.0 to 2.0.20 in [(#299)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/299)
 - chore(deps): bump org.jetbrains.kotlin:kotlin-stdlib from 2.0.0 to 2.0.20 in [(#297)](https://github.com/redhat-developer/intellij-common-ui-test-library/pull/297)

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
