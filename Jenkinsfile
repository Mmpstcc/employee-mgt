pipeline {
    agent any

    environment {
        DOCKER_USER   = "your-dockerhub-username"
        BACKEND_IMG   = "${DOCKER_USER}/emp-backend"
        FRONTEND_IMG  = "${DOCKER_USER}/emp-frontend"
        TAG           = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Cloning source code...'
                checkout scm
            }
        }

        stage('Backend Test') {
            steps {
                echo '🧪 Running backend tests...'
                dir('backend') {
                    sh 'mvn clean test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Backend Build') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('Frontend Build') {
            steps {
                echo '⚛️ Installing frontend dependencies...'
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '🐳 Building Docker images...'
                sh "docker build -t ${BACKEND_IMG}:${TAG}  -t ${BACKEND_IMG}:latest  ./backend"
                sh "docker build -t ${FRONTEND_IMG}:${TAG} -t ${FRONTEND_IMG}:latest ./frontend"
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DUSER',
                    passwordVariable: 'DPASS'
                )]) {
                    sh "echo $DPASS | docker login -u $DUSER --password-stdin"
                    sh "docker push ${BACKEND_IMG}:${TAG}  && docker push ${BACKEND_IMG}:latest"
                    sh "docker push ${FRONTEND_IMG}:${TAG} && docker push ${FRONTEND_IMG}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo '🚀 Deploying to Minikube...'
                sh """
                    kubectl set image deployment/emp-backend  emp-backend=${BACKEND_IMG}:${TAG}  --record || \
                    kubectl apply -f k8s/
                    kubectl set image deployment/emp-frontend emp-frontend=${FRONTEND_IMG}:${TAG} --record
                """
                sh 'kubectl rollout status deployment/emp-backend  --timeout=120s'
                sh 'kubectl rollout status deployment/emp-frontend --timeout=120s'
            }
        }
    }

    post {
        success { echo "✅ Build #${TAG} deployed successfully!" }
        failure {
            echo "❌ Build failed — rolling back..."
            sh 'kubectl rollout undo deployment/emp-backend  || true'
            sh 'kubectl rollout undo deployment/emp-frontend || true'
        }
        always { sh 'docker logout || true' }
    }
}
