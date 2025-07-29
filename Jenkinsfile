pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/JeanHeberth/FinancasPessoais.git', branch: 'main'
                git url: 'https://github.com/JeanHeberth/FinancasPessoais.git', branch: 'develop'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
    }

    post {
        always {
            junit '**/build/test-results/test/*.xml'
        }
    }
}
