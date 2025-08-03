pipeline {
    agent any

    tools {
        gradle 'Gradle_8' // Nome da versão configurada no Jenkins (altere se necessário)
    }

    environment {
        GRADLE_OPTS = "-Dorg.gradle.jvmargs='-Xmx1024m'"
    }

    stages {

        stage('📦 Build') {
            steps {
                echo '🔧 Etapa de build iniciada...'
                sh './gradlew clean build -x test'
            }
        }

        stage('✅ Testes Automatizados') {
            steps {
                echo '🧪 Rodando testes...'
                sh './gradlew test'
            }
        }

        stage('📊 Verificação de Qualidade') {
            steps {
                echo '🔍 Executando validações (check, lint, etc)...'
                sh './gradlew check'
            }
        }

        stage('🚀 Deploy (main)') {
            when {
                branch 'main'
            }
            steps {
                echo '📤 Enviando build para produção...'
                // Exemplo fictício
                sh 'echo "Deploy finalizado com sucesso!"'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline concluído com sucesso!'
        }
        failure {
            echo '❌ Falha durante o pipeline. Verifique os logs.'
        }
    }
}
