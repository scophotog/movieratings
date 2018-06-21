node {
  stage('Checkout') {
    checkout scm
  }

  stage('Build') {
    try {
      sh './gradlew --refresh-dependencies clean assembleDebug'
      archiveArtifacts artifacts: 'app/build/outputs/apk/*.apk', fingerprint: true
      currentBuild.result = 'SUCCESS'
    } catch(error) {
      currentBuild.result = 'FAILURE'
    }
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

  stage('Archive') {
    archiveArtifacts 'app/build/outputs/apk/*'
  }
}
