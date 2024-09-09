pipeline {
    agent any 
        
    tools {
        maven 'maven3'
        jdk 'jdk17'
    }
    
    environment {
        SCANNER_HOME=tool'sonar-scanner'
    }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/arpita199812/Ekart-project.git'
            }
        }
    
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Unit-test') {
            steps {
                sh 'mvn test -DskipTests=true'
            }
        }
        stage('SonarQube-Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar-server') {
                    sh '''$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=Ekart -Dsonar.projectKey=Ekart \
                    -Dsonar.java.binaries=target'''
                }
            }
        }
        stage('OWSAP Dependency-Check') {
            steps {
                script {
                    env.DEPENDENCYCHECK_APIKEY = 'fa019c6f-45d4-4390-9bca-f24617ee941f'
                    dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'DC'
                    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                }
            }
        }
        stage('Build & Package') {
            steps {
                sh 'mvn package -DskipTests=true'
            }
        }
        stage('Artifact Upload to Nexus') {
            steps {
                withMaven(globalMavenSettingsConfig: 'setting-maven', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                  sh 'mvn deploy -DskipTests=true'
                }
            }
        }
        stage('Docker Build & Tag name') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-hub-id', toolName: 'docker') {
                        sh 'docker build -t arpita199812/ekart:latest -f docker/Dockerfile .'
                    }
                }
            }
        }
        stage('Trivy Scan') {
            steps {
                sh 'trivy image arpita199812/ekart:latest > trivy-report.txt '
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-hub-id', toolName: 'docker') {
                        sh 'docker push arpita199812/ekart:latest'
                    }
                }
            }
        }
        stage('Kubernetes Deploy') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'k8-token', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://10.0.2.184:6443') {
                    sh 'kubectl apply -f deploymentservice.yml'
                    sh 'kubectl get svc -n webapps'
                }
            }
        }
    }
  }
