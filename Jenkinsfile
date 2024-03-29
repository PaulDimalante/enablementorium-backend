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
    options {
      gitLabConnection('gitlab')
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
        expression {
            return env.GIT_BRANCH != 'origin/master'
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
        expression {
            return env.GIT_BRANCH != 'origin/master'
        }
      }
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
      when {
        expression {
            return env.GIT_BRANCH != 'origin/master'
        }
      }
      steps {
        withSonarQubeEnv('Sonar_GCP') {
            sh './gradlew check jacocoTestCoverageVerification sonar -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.projectName=reference-ms-crud'
        }
      }
    }
    stage("sonar-qa") {
      when {
        expression {
            return env.GIT_BRANCH != 'origin/master'
        }
      }
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
        updateGitlabCommitStatus name: 'sonar', state: 'success'
        acceptGitLabMR()
      }
    }
    stage('nexus-deliver') {
      when {
        expression {
            return env.GIT_BRANCH == 'origin/develop'
        }
      }
      steps {
        sh '''
            ./gradlew -PnexusUsername=$NEXUSCRED_USR -PnexusPassword=$NEXUSCRED_PSW uploadArchives
        '''
        updateGitlabCommitStatus name: 'nexus', state: 'success'
      }
    }
    stage('release') {
      when {
        expression {
            return env.GIT_BRANCH == 'origin/master'
        }
      }
      steps {
        sh '''
            ./gradlew -PnexusUsername=$NEXUSCRED_USR -PnexusPassword=$NEXUSCRED_PSW uploadArchives release
        '''
        updateGitlabCommitStatus name: 'nexus', state: 'success'
      }
    }
    stage('deploy-develop') {
      when {
        expression {
            return env.GIT_BRANCH == 'origin/develop'
        }
      }
      steps {
        sh '''
            ./gradlew -PCF_USR=$CF_USR -PCF_PSW=$CF_PSW -PCF_SPACE="dev - 1000228994" cf-push
        '''
        updateGitlabCommitStatus name: 'cf-push', state: 'success'
      }
    }
    stage('deploy-master') {
      when {
        expression {
            return env.GIT_BRANCH == 'origin/master'
        }
      }
      steps {
        sh '''
            ./gradlew -PCF_USR=$CF_USR -PCF_PSW=$CF_PSW -PCF_SPACE="dev - 1000228994" cf-push-blue-green
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
