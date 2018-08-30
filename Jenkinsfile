node {
  stage('Checkout') {
    checkout scm
  }

  stage('Code Analysis') {
    sh './gradlew lintDebug -PAPI_KEY="$API_KEY"'
  }

  stage('Build') {
    sh './gradlew -PAPI_KEY="$API_KEY" --refresh-dependencies clean assembleDebug assembleAndroidTest'
  }

  stage('Unit Tests') {
    sh './gradlew -PAPI_KEY="$API_KEY" testDebug'
  }

  stage('UI Tests') {
    sh 'ANDROID_SERIAL=emulator-5554'
    sh './gradlew -PAPI_KEY="$API_KEY" connectedAndroidTest'
  }

  stage('Archive') {
    archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
  }

  stage('Generate Reports') {
    androidLint canComputeNew: false,
        defaultEncoding: '',
        healthy: '',
        pattern: '**/reports/lint-results-debug.xml',
        unHealthy: ''
    step([$class: "JUnitResultArchiver",
        testResults: "**/app/build/test-results/testDebugUnitTest/*.xml"])
  }
}
