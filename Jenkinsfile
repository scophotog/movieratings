node {
  stage('Checkout') {
    checkout scm
  }

  stage('Build') {
    sh './gradlew --refresh-dependencies clean assembleDebug'
    archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
  }

  stage('Unit Tests') {
    sh './gradlew testDebug'
  }

  stage('Code Analysis') {
    sh './gradlew lintDebug'
    androidLint canComputeNew: false,
        defaultEncoding: '',
        healthy: '',
        pattern: '**/reports/lint/lint-result.xml',
        unHealthy: ''
    step([$class: "JUnitResultArchiver",
        testResults: "**/app/build/test-results/testDebugUnitTest/*.xml"])
  }

}
