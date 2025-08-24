pipeline {
    agent any
    parameters {
        choice(name: 'ENV_PROFILE', choices: ['sit', 'uat', 'prod'], description: 'Select the environment for WAR generation (SIT, UAT, PROD)')
    }
    environment {
        ENV_PROFILE = "${params.ENV_PROFILE}"
    }
    tools {
        jdk 'JDK17'
        gradle 'Gradle'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/yolandadalin/jenkins_properties_demo.git'
            }
        }
        stage('Build WAR') {
            steps {
                sh './gradlew clean war -PenvProfile=${ENV_PROFILE}'
            }
        }
        stage('Deploy to JBoss') {
            steps {
                sh 'cp build/libs/hello.war /Users/yolanda/Desktop/jboss-eap-8.0/standalone/deployments/'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/hello.war', allowEmptyArchive: true
        }
    }
}