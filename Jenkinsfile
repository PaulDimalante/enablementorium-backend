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
      when {
        not {
            branch 'master'
        }
      }
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
      when {
        not {
            branch 'master'
        }
      }
      steps {
        script {
          try {
            sh 'printenv'
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
      when {
        not {
            branch 'master'
        }
      }
      steps {
        withSonarQubeEnv('Sonar_GCP') {
            sh './gradlew check jacocoTestCoverageVerification sonar -Dsonar.host.url=https://sonar.unreleased.work'
            updateGitlabCommitStatus name: 'sonar', state: 'success'
        }
        acceptGitLabMR()
      }
    }
    stage("sonar-qa") {
      when {
        not {
            branch 'master'
        }
      }
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
    stage('nexus-deliver') {
      when {
        branch '*/develop'
      }
      steps {
        sh '''
            ./gradlew -PnexusUsername=$NEXUSCRED_USR -PnexusPassword=$NEXUSCRED_PSW uploadArchives
        '''
        updateGitlabCommitStatus name: 'nexus', state: 'success'
      }
    }
    stage('deploy-develop') {
      when {
        branch '**/develop'
      }
      steps {
        sh '''
            ./gradlew cf-push
        '''
        updateGitlabCommitStatus name: 'cf-push', state: 'success'
      }
    }
    stage('deploy-master') {
      when {
        branch 'master'
      }
      steps {
        sh '''
            ./gradlew cf-push-blue-green
        '''
        updateGitlabCommitStatus name: 'cf-push-production', state: 'success'
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
