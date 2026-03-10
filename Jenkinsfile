pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Aryan-Dalal/Event_Mangement.git'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat 'copy target\\eventManagement-0.0.1-SNAPSHOT.war C:\\Users\\dalal\\Downloads\\apache-tomcat-10.1.xx\\webapps'
            }
        }

    }
}