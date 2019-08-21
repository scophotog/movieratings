pipeline {
  agent any
  stages {
    stage('Code Analysis') {
      post {
        always {
          androidLint(pattern: '**/reports/lint-results-debug.xml')

        }

      }
      steps {
        sh './gradlew lintDebug -PAPI_KEY=$API_KEY'
      }
    }
    stage('Build') {
      post {
        success {
          archiveArtifacts(artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true)

        }

      }
      steps {
        sh './gradlew -PAPI_KEY=$API_KEY --refresh-dependencies assembleDebug assembleAndroidTest'
      }
    }
    stage('Unit Tests') {
      post {
        always {
          junit '**/app/build/test-results/testDebugUnitTest/*.xml'

        }

      }
      steps {
        sh './gradlew -PAPI_KEY=$API_KEY testDebug'
      }
    }
    stage('UI Tests') {
      when {
        expression {
          return params.RUN_UITEST
        }

      }
      steps {
        script {
          try {
            sh 'ANDROID_SERIAL=emulator-5554'
            sh './gradlew -PAPI_KEY=$API_KEY connectedAndroidTest'
          }
          catch (exc) {
            echo 'Testing failed!'
            currentBuild.result = 'UNSTABLE'
          }
        }

      }
    }
  }
  parameters {
    booleanParam(name: 'RUN_UITEST', defaultValue: false, description: 'Run UI Tests?')
  }
}
