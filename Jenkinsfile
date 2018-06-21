node {
  stage 'Checkout'

  checkout scm

  stage('Build') {
    try {
      sh './gradlew --refresh-dependencies clean assemble'
      lock('emulator') {
        sh './gradlew connectedCheck'
      }

      currentBuild.result = 'SUCCESS'
    } catch(error) {
      currentBuild.result = 'FAILURE'
    } finally {
      junit '**/test-results/**/*.xml'
    }
  }

  stage('Archive') {
    archiveArtifacts 'app/build/outputs/apk/*'
  }
}
