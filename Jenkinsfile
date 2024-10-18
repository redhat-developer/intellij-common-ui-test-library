#!/usr/bin/env groovy

node('rhel7') {
	def javaHome = tool 'openjdk-17'
	env.JAVA_HOME = "${javaHome}"

    stage('Checkout repo') {
        deleteDir()
        git url: 'https://github.com/redhat-developer/intellij-common-ui-test-library',
                branch: "${sha1}"
    }

    def props = readProperties file: 'gradle.properties'
    def isSnapshot = props['projectVersion'].contains('-SNAPSHOT')

    stage('Build') {
        sh "./gradlew build"
    }

    stage('Deploy') {
        withCredentials([usernamePassword(credentialsId: 'Nexus-IJ-Credentials', usernameVariable: 'USER', passwordVariable: 'PASSWORD')]) {
            if (isSnapshot) {
                sh './gradlew publish -PnexusUser=$USER -PnexusPassword=$PASSWORD'
            } else {
                sh './gradlew publish closeAndReleaseStagingRepositories -PnexusUser=$USER -PnexusPassword=$PASSWORD'
            }
        }
    }
}
