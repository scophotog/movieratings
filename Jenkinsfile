pipeline {
  agent any
  
  stages {
    stage('Code Analysis') {
      steps {        
        sh './gradlew lintDebug -PAPI_KEY=$API_KEY'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew -PAPI_KEY=$API_KEY --refresh-dependencies assembleDebug assembleAndroidTest'
      }
    }

    stage('Unit Tests') {
      steps {
        sh './gradlew -PAPI_KEY=$API_KEY testDebug'
      }
    }

    stage('UI Tests') {
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

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
      }
    }

    stage('Generate Reports') {
      steps {
        androidLint canComputeNew: false,
            defaultEncoding: '',
            healthy: '',
            pattern: '**/reports/lint-results-debug.xml',
            unHealthy: ''
        step([$class: "JUnitResultArchiver",
            testResults: "**/app/build/test-results/testDebugUnitTest/*.xml"])
      }
    }
  }
}
