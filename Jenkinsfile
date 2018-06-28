node {
  stage('Checkout') {
    checkout scm
  }

  stage('Code Analysis') {
    sh './gradlew lintDebug'
  }

  stage('Build') {
    sh './gradlew --refresh-dependencies clean assembleDebug'
  }

  stage('Unit Tests') {
    sh './gradlew testDebug'
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
