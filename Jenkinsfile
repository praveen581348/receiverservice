pipeline {
    // 1. AGENT
    // Run on any available agent. Assumes 'mvn', 'docker', and 'kubectl' are installed
    // in your Jenkins image (praveen581348/jenkins-dind:25).
    agent any

    // 2. ENVIRONMENT
    // Define variables we'll use throughout the pipeline
    environment {
        // We use the Jenkins build number for a unique, traceable image tag
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
        IMAGE_NAME      = "praveen581348/receiverservice"

        // Kubernetes deployment details (from your deployment.yaml)
        K8S_DEPLOYMENT  = "receiverservice-deployment"
        K8S_CONTAINER   = "receiverservice" // The 'name:' of the container in your yaml
        K8S_NAMESPACE   = "receiver"
    }

    // 3. STAGES
    stages {
        stage('Checkout Code') {
            steps {
                // Clones the specific public repository and branch
                git url: 'https://github.com/praveen581348/receiverservice.git', branch: 'master'
            }
        }

        stage('Build & Push to Nexus') {
            steps {
                echo '--- 1. BUILDING & DEPLOYING MAVEN ARTIFACT ---'
                
                // This block securely provides the 'nexus-settings' file (from Managed files)
                // and injects the 'nexus_cred' credentials as environment variables.
                configFileProvider([configFile(fileId: 'nexus-settings', variable: 'MAVEN_SETTINGS')]) {
                    withCredentials([usernamePassword(credentialsId: 'nexus_cred', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                        
                        // Use -s $MAVEN_SETTINGS to force Maven to use our settings file
                        // This fixes the 401 Unauthorized error
                        sh "mvn clean deploy -DskipTests -s $MAVEN_SETTINGS"
                    }
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo '--- 2. BUILDING DOCKER IMAGE ---'
                
                // Build the image using the variables defined above
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                
                echo '--- 3. PUSHING DOCKER IMAGE ---'
                
                // This block securely provides the Docker Hub credentials
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    // Log in using the injected credentials
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    
                    // Push the image
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "--- 4. DEPLOYING TO KUBERNETES (NAMESPACE: ${K8S_NAMESPACE}) ---"
                
                // This is the non-Helm way to update an image.
                // It finds the deployment and sets the new image for the specified container.
                // This works because your Jenkins Pod is using the 'jenkins-sa' Service Account.
                sh "kubectl set image deployment/${K8S_DEPLOYMENT} ${K8S_CONTAINER}=${IMAGE_NAME}:${IMAGE_TAG} -n ${K8S_NAMESPACE}"
                
                // This command waits for the rolling update to complete successfully.
                // If the new pod fails to start, this step will fail the build.
                sh "kubectl rollout status deployment/${K8S_DEPLOYMENT} -n ${K8S_NAMESPACE}"
                
                echo "Deployment successful!"
            }
        }
    }
    
    // 4. POST-BUILD ACTIONS
    // Runs after all stages are complete
    post {
        success {
            echo 'Pipeline Succeeded!'
        }
        failure {
            echo 'Pipeline Failed! Check logs.'
        }
    }
}