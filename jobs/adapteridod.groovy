
import static helpers.JobHelper.*

job('idod-adapter') {
    def gerritrepo = 'idod/extras/adapter'
    description 'Build and test the app.'
    jdk ('jdk8')
    configure gerritParameters ('refs/head/master')
    configure gerritConfigurations(gerritrepo)
    configure gerritTrigger (gerritrepo)
    configure artifactArchiver ('build/libs/*,build/distributions/*,**/build/libs/*,**/build/distributions/*')
    configure artifactFingerprinter ()
    // configure otherConfigurations ('40', 'false', 'windows') // quietPeriod, canRoam
    //gradleSetup ('build')

    steps {
        gradle {
            useWrapper true
            makeExecutable false
            fromRootBuildScriptDir false
            tasks 'build'
        }
    }
}
