pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/Aryan-Dalal/Event_Mangement.git'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Run Application') {
            steps {
                bat 'java -jar target/*.jar'
            }
        }

    }
}