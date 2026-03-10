pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build Application') {
            steps {
                echo 'Building the Spring Boot project using Maven...'
                bat 'mvn clean package'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying WAR file to Tomcat server...'
                bat 'copy /Y target\\eventManagement-0.0.1-SNAPSHOT.war C:\\Users\\dalal\\Downloads\\apache-tomcat-10.1.52-windows-x64\\apache-tomcat-10.1.52\\webapps'
            }
        }

        stage('Deployment Completed') {
            steps {
                echo 'Application successfully deployed to Tomcat!'
            }
        }

    }
}