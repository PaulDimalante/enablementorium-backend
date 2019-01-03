updateGitlabCommitStatus state: 'pending'
pipeline {
  agent {
    docker {
      image 'docker.unreleased.work/gradle-cf-node-ng-headless:latest'
      registryUrl 'https://docker.unreleased.work'
      registryCredentialsId 'docker-credentials'
      args '-v /root/.m2:/root/.m2'
    }
  }
  environment {
    CF = credentials('pws-credentials')
    NEXUSCRED = credentials('472bcc5d-035b-44a9-9fda-d6e6a9f22f05')
  }
  stages {
    stage('build') {
      steps {
        updateGitlabCommitStatus state: 'running'
      }
    }
    stage('unit-test') {
      steps {
        script {
          try {
            sh './gradlew clean test'
            updateGitlabCommitStatus name: 'unit test', state: 'success'
          } catch (exc) {
            // this is so we can capture the results in 'finally' below
            updateGitlabCommitStatus name: 'unit test failed', state: 'failed'
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
            updateGitlabCommitStatus name: 'integration test', state: 'success'
          } catch (exc) {
            // this is so we can capture the results in 'finally' below
            updateGitlabCommitStatus name: 'integration test failed', state: 'failed'
            throw exec
          } finally {
            junit '**/build/test-results/integrationTest/*.xml'
          }
        }
      }
    }
    stage('sonar') {
      steps {
        sh './gradlew check jacocoTestCoverageVerification sonar -Dsonar.host.url=https://sonar.unreleased.work -Dsonar.branch.name=' + env.BRANCH_NAME
        updateGitlabCommitStatus name: 'sonar', state: 'success'
        acceptGitLabMR()
      }
    }
    stage('nexus-deliver') {
      when {
        anyOf {
            branch 'develop';
            branch 'master'
        }
      }
      steps {
        withCredentials([usernamePassword(credentialsId: '472bcc5d-035b-44a9-9fda-d6e6a9f22f05', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh './gradlew -PUSERNAME=$USERNAME -PPASSWORD=$PASSWORD -x test bootJar uploadArchives'
        }
        updateGitlabCommitStatus name: 'nexus', state: 'success'
      }
    }
  }
  post {
    always {
      deleteDir()
    }
    success {
      updateGitlabCommitStatus state: 'success'
    }
    failure {
      updateGitlabCommitStatus state: 'failed'
    }
  }
}
