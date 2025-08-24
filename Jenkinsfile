pipeline {
    agent any
    parameters {
        choice(name: 'ENV_PROFILE', choices: ['sit', 'uat', 'prod'], description: 'Select the environment for WAR generation (SIT, UAT, PROD)')
    }
    environment {
        ENV_PROFILE = "${params.ENV_PROFILE}"
    }
    tools {
        jdk 'JDK17' // 對應 Global Tool Configuration 中的 JDK 名稱
        gradle 'Gradle' // 對應 Gradle 名稱
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/yolandadalin/jenkins_properties_demo.git', credentialsId: 'git-credentials' // 替換 URL 和憑證 ID
            }
        }
        stage('Build WAR') {
            steps {
                sh './gradlew clean war -PenvProfile=${ENV_PROFILE}'
            }
        }
        stage('Deploy to JBoss') {
            steps {
                script {
                    def jbossDeployDir
                    if (env.ENV_PROFILE == 'sit') {
                        jbossDeployDir = '~/jboss-eap-8.0-sit/standalone/deployments/'
                    } else if (env.ENV_PROFILE == 'uat') {
                        jbossDeployDir = '~/jboss-eap-8.0-uat/standalone/deployments/'
                    } else {
                        jbossDeployDir = '~/jboss-eap-8.0-prod/standalone/deployments/'
                    }
                    sh "cp build/libs/hello.war ${jbossDeployDir}"
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/hello.war', allowEmptyArchive: true
        }
    }
}
