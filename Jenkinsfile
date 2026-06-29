pipeline {
    agent any

    environment {
        DOCKER_USER  = "mmpstscc"
        BACKEND_IMG  = "${DOCKER_USER}/emp-backend"
        FRONTEND_IMG = "${DOCKER_USER}/emp-frontend"
        TAG          = "${BUILD_NUMBER}"
    }

    stages {

        // ─────────────────────────────────────────
        stage('Checkout') {
        // ─────────────────────────────────────────
            steps {
                echo '📥 Cloning source code...'
                checkout scm
            }
        }

        // ─────────────────────────────────────────
        stage('Backend Test') {
        // ─────────────────────────────────────────
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

        // ─────────────────────────────────────────
        stage('Backend Build') {
        // ─────────────────────────────────────────
            steps {
                echo '📦 Building backend JAR...'
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        // ─────────────────────────────────────────
        stage('Frontend Build') {
        // ─────────────────────────────────────────
            steps {
                echo '⚛️ Building frontend...'
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        // ─────────────────────────────────────────
        stage('Docker Build') {
        // ─────────────────────────────────────────
        // ✅ Login BEFORE build so Docker Hub base
        //    images (node:20-alpine etc.) can be
        //    pulled without hitting rate limits
        // ─────────────────────────────────────────
            steps {
                echo '🐳 Building Docker images...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DUSER',
                    passwordVariable: 'DPASS'
                )]) {
                    sh 'echo "$DPASS" | docker login -u "$DUSER" --password-stdin'

                    sh "docker build -t ${BACKEND_IMG}:${TAG} -t ${BACKEND_IMG}:latest ./backend"
                    sh "docker build -t ${FRONTEND_IMG}:${TAG} -t ${FRONTEND_IMG}:latest ./frontend"
                }
            }
        }

        // ─────────────────────────────────────────
        stage('Docker Push') {
        // ─────────────────────────────────────────
            steps {
                echo '⬆️ Pushing images to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DUSER',
                    passwordVariable: 'DPASS'
                )]) {
                    sh 'echo "$DPASS" | docker login -u "$DUSER" --password-stdin'

                    sh "docker push ${BACKEND_IMG}:${TAG}  && docker push ${BACKEND_IMG}:latest"
                    sh "docker push ${FRONTEND_IMG}:${TAG} && docker push ${FRONTEND_IMG}:latest"
                }
            }
        }

        // ─────────────────────────────────────────
        stage('Docker Cleanup') {
        // ─────────────────────────────────────────
            steps {
                echo '🧹 Cleaning up local Docker images...'
                sh '''
                    docker image prune -af
                    docker container prune -f
                    docker builder prune -af
                    docker logout
                '''
            }
        }

        // ─────────────────────────────────────────
        stage('Deploy to Kubernetes') {
        // ─────────────────────────────────────────
            steps {
                echo '🚀 Deploying to Kubernetes...'
                withKubeConfig([credentialsId: 'kubeconfig']) {
                    sh """
                        # Apply manifests in correct dependency order
                        kubectl apply -f k8s/mongodb-pv.yaml
                        kubectl apply -f k8s/mongodb.yaml

                        # Wait for MongoDB to be ready before starting backend
                        kubectl rollout status deployment/mongodb --timeout=120s || true

                        kubectl apply -f k8s/backend.yaml
                        kubectl apply -f k8s/frontend.yaml
                        kubectl apply -f k8s/Ingress.yaml
                        kubectl apply -f k8s/hpa.yaml

                        # Update images to this build's tag
                        kubectl set image deployment/emp-backend  emp-backend=${BACKEND_IMG}:${TAG}
                        kubectl set image deployment/emp-frontend emp-frontend=${FRONTEND_IMG}:${TAG}

                        # Wait for rollouts
                        kubectl rollout status deployment/emp-backend  --timeout=140s
                        kubectl rollout status deployment/emp-frontend --timeout=140s

                        echo "=== Pods ==="
                        kubectl get pods -o wide

                        echo "=== Services ==="
                        kubectl get svc

                        echo "=== HPA ==="
                        kubectl get hpa
                    """
                }
            }
        }
    }

    // ─────────────────────────────────────────
    post {
    // ─────────────────────────────────────────
        success {
            echo "✅ Pipeline succeeded — images: ${BACKEND_IMG}:${TAG} | ${FRONTEND_IMG}:${TAG}"
        }
        failure {
            echo '❌ Pipeline failed. Check the stage logs above.'
        }
        always {
            sh 'docker logout || true'
        }
    }
}
        
        
        
