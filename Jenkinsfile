pipeline {
    agent any
    tools {
        maven 'maven' // Nom de la configuration Maven dans Jenkins
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub') // Remplacez 'docker-hub' par l'ID de vos credentials
        IMAGE_NAME = 'yassinekb1'
        BUILD_VERSION = "${BUILD_NUMBER}" // Numéro de build Jenkins pour versionner vos images
    }
    stages {
        stage('Build Application') {
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:${BUILD_VERSION}")
                }
            }
        }
        stage('Scan Docker Image') {
            steps {
                script {
                    sh """
                    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                    aquasec/trivy:latest image --exit-code 0 \
                    --severity LOW,MEDIUM,HIGH,CRITICAL \
                    ${IMAGE_NAME}:${BUILD_VERSION}
                    """
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKERHUB_CREDENTIALS}") {
                        dockerImage.push("${BUILD_VERSION}")
                        dockerImage.push('latest') // Optionnel : Marquer l'image comme "latest"
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f k8s/k8s-deployment.yml'
                    sh 'kubectl apply -f k8s/k8s-service.yml'
                    sh 'kubectl apply -f k8s/mysql-deployment.yml'
                }
            }
        }
    }
    post {
        always {
            script {
                dockerImage?.delete()
            }
        }
    }
}