pipeline {
    agent any

    stages {
        stage('Build and test') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw clean verify'
                    } else {
                        bat 'mvnw.cmd clean verify'
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}