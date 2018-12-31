#!/usr/bin/env groovy
pipeline {
  agent {
    docker {
      image 'docker.unreleased.work/maven-cf-node-headless:latest'
      registryUrl 'https://docker.unreleased.work'
      registryCredentialsId 'docker-credentials'
      args '-v /root/.m2:/root/.m2'
    }
  }
  environment {
    CF = credentials('pws-credentials')
  }
  stages {
    stage('unit-test') {
      steps {
        script {
          try {
            sh './gradlew clean test'
          } catch (exc) {
            // this is so we can capture the results in 'finally' below
              throw exec
          } finally {
            junit '**/build/test-results/test/*.xml'
          }
        }
      }
    }
    stage('integration-test') {
      steps {
        script {
          try {
            sh './gradlew clean integrationTest'
          } catch (exc) {
            // this is so we can capture the results in 'finally' below
            throw exec
          } finally {
            junit '**/build/test-results/integrationTest/*.xml'
          }
        }
      }
    }
    stage('sonar') {
      steps {
        sh './gradlew check jacocoTestCoverageVerification sonar -Dsonar.host.url=https://sonar.unreleased.work -Dsonar.branch=${env.BRANCH_NAME}'
      }
    }
    stage('nexus-deliver') {
      when {
        branch 'develop'
      }
      steps {
        withCredentials([usernamePassword(credentialsId: '472bcc5d-035b-44a9-9fda-d6e6a9f22f05', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh './gradlew -PUSERNAME=$USERNAME -PPASSWORD=$PASSWORD uploadArchives'
        }
      }
    }
  }
}
